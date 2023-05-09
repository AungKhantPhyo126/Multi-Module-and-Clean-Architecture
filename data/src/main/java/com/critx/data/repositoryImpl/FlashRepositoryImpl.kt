package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.flashSale.FlashDataSource
import com.critx.data.network.api.FlashSaleService
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.dailygoldAndPrice.asDomain
import com.critx.data.network.dto.flashSales.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.flashSales.UserPointsDomain
import com.critx.domain.repository.FlashSaleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class FlashRepositoryImpl @Inject constructor(
    private val flashDataSource: FlashDataSource
) : FlashSaleRepository {
    override fun addToFlashSale(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
        productIds: List<RequestBody>,
        image: MultipartBody.Part
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        flashDataSource.addToFlashSale(
                            token,
                            title,
                            discount_amount,
                            time_from,
                            time_to,
                            productIds,
                            image
                        ).asDomain()
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

    override fun getUserPoint(token: String): Flow<Resource<UserPointsDomain>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        flashDataSource.getUserPoint(
                            token,
                        ).asDomain()
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

    override fun manualPointsAddOrReduce(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody
    ): Flow<Resource<SimpleData>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        flashDataSource.manualPointsAddOrReduce(
                            token,
                           title, discount_amount, time_from, time_to
                        ).asDomain()
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