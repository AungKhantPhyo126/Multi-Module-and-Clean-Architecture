package com.critx.data.repository

import com.critx.commonkotlin.util.Resource
import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.network.datasource.AuthNetWorkDataSourceImpl
import com.critx.data.network.dto.auth.asDomain
import com.critx.domain.model.LogInSuccess
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
            } catch (e: Exception) {
                emit(Resource.Error(e.message))
            } catch (e: HttpException) {
                emit(Resource.Error(e.message))
            } catch (e: IOException) {
                emit(Resource.Error(e.message))
            }
        }
}