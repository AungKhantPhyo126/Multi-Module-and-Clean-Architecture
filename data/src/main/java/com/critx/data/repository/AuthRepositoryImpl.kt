package com.critx.data.repository

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.network.datasource.AuthNetWorkDataSourceImpl
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.auth.asDomain
import com.critx.domain.model.LogInSuccess
import com.critx.domain.model.Profile
import com.critx.domain.model.SimpleData
import com.critx.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authNetWorkDataSource: AuthNetWorkDataSource) :
    AuthRepository {
    override fun login(name: String, password: String): Flow<Resource<LogInSuccess>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        authNetWorkDataSource.login(name, password).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            }catch (e: Exception) {
                emit(Resource.Error(e.message?:"Unhandled Error"))
            }
        }

    override fun logout(token: String): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        authNetWorkDataSource.logout(token).response.asDomain()
                    )
                )
            }catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            }catch (e: Exception) {
                emit(Resource.Error(e.message?:"Unhandled Error"))
            }
        }

    override fun refreshToken(token: String): Flow<Resource<LogInSuccess>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        authNetWorkDataSource.refreshToken(token).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            }catch (e: Exception) {
                emit(Resource.Error(e.message?:"Unhandled Error"))
            }
        }

    override fun getProfile(token: String): Flow<Resource<Profile>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        authNetWorkDataSource.getProfile(token).data.asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            }catch (e: Exception) {
                emit(Resource.Error(e.message?:"Unhandled Error"))
            }
        }
}