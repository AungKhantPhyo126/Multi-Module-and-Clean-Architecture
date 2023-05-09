package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.box.BoxNetWorkDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.box.asDomain
import com.critx.data.network.dto.dailygoldAndPrice.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.box.BoxScanDomain
import com.critx.domain.model.box.BoxWeightDomain
import com.critx.domain.repository.BoxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BoxRepositoryImpl @Inject constructor(
    private val boxNetWorkDataSource: BoxNetWorkDataSource
) : BoxRepository {
    override fun getBoxWeight(
        token: String,
        boxIdList: List<String>
    ): Flow<Resource<List<BoxWeightDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        boxNetWorkDataSource.getBoxWeight(token, boxIdList).map { it.asDomain() }
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun getBoxData(token: String, boxCode: String): Flow<Resource<BoxScanDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        boxNetWorkDataSource.getBoxData(token, boxCode).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun arrangeBox(token: String, boxes: List<String>): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        boxNetWorkDataSource.arrangeBox(token, boxes).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }
}