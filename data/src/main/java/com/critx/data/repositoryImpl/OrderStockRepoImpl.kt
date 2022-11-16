package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.orderStock.OrderStockDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.orderStock.asDomain
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkedStocksWithPaging
import com.critx.domain.repository.OrderStockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OrderStockRepoImpl @Inject constructor(
    private val orderStockDataSource: OrderStockDataSource
) :OrderStockRepository{
    override fun getBookMarkStockList(
        token: String,
        jewlleryType: String,
        page: Int
    ): Flow<Resource<BookMarkedStocksWithPaging>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        orderStockDataSource.getBookMarkStockList(token,jewlleryType,page).asDomain()
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