package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.orderStock.OrderStockDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.orderStock.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkStockInfoDomain
import com.critx.domain.model.orderStock.BookMarkedStocksWithPaging
import com.critx.domain.repository.OrderStockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderStockRepoImpl @Inject constructor(
    private val orderStockDataSource: OrderStockDataSource
) : OrderStockRepository {
    override fun getBookMarkStockList(
        token: String,
        jewlleryType: String,
        isItemFromGs: String,
        page: Int
    ): Flow<Resource<BookMarkedStocksWithPaging>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        orderStockDataSource.getBookMarkStockList(
                            token,
                            jewlleryType,
                            isItemFromGs,
                            page
                        )
                            .asDomain()
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

    override fun getGsNewItems(
        token: String,
        page: Int,
        jewelleryType: String
    ): Flow<Resource<BookMarkedStocksWithPaging>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        orderStockDataSource.getGsNewItems(
                            token,
                            page,
                            jewelleryType
                        )
                            .asDomain()
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

    override fun getBookMarkStockInfo(
        token: String,
        bookMarkId: String
    ): Flow<Resource<List<BookMarkStockInfoDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        orderStockDataSource.getBookMarkStockInfoList(token, bookMarkId)
                            .map { it.asDomain() }
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

    override fun orderStock(
        token: String,
        bookMarkAvgYwae: MultipartBody.Part?,
        orderAvgYwae: MultipartBody.Part?,
        bookMarkJewelleryTypeId: MultipartBody.Part?,
        bookMarkImage: MultipartBody.Part?,
        goldQuality: MultipartBody.Part,
        goldSmith: MultipartBody.Part,
        bookMarkId: MultipartBody.Part?,
        gsNewItemId: MultipartBody.Part?,
        equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        jewellery_type_size_id: List<MultipartBody.Part>,
        order_qty: List<MultipartBody.Part>,
        sample_id: List<MultipartBody.Part>?,
        is_important:MultipartBody.Part?,
        custom_category_name:MultipartBody.Part?,

        ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        orderStockDataSource.orderStock(
                            token,
                            bookMarkAvgYwae,
                            orderAvgYwae,
                            bookMarkJewelleryTypeId,
                            bookMarkImage,
                            goldQuality,
                            goldSmith,
                            bookMarkId,
                            gsNewItemId,
                            equivalent_pure_gold_weight_kpy,
                            jewellery_type_size_id,
                            order_qty,
                            sample_id,
                            is_important,
                            custom_category_name
                        ).response.asDomain()
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