package com.critx.data.network.datasource

import com.critx.data.datasource.orderStock.OrderStockDataSource
import com.critx.data.network.api.OrderStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.orderStock.BookMarkStockDto
import com.critx.data.network.dto.orderStock.BookMarkStockInfoDto
import com.critx.data.network.dto.orderStock.BookMarkedStocksResponse
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.getMessage
import okhttp3.MultipartBody
import javax.inject.Inject

class OrderStockDataSourceImpl @Inject constructor(
    private val orderStockService: OrderStockService
) : OrderStockDataSource {
    override suspend fun getBookMarkStockList(
        token: String,
        jewelleryType: String,
        page: Int
    ): BookMarkedStocksResponse {
        val response = orderStockService.getBookMarks(token, jewelleryType, page)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        getErrorString(
                            response.errorBody()
                                ?.parseError<CreateCategoryError>()?.response?.message?.getMessage()!!
                        )
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

    override suspend fun getBookMarkStockInfoList(
        token: String,
        bookMarkId: String
    ): List<BookMarkStockInfoDto> {
        val response = orderStockService.getBookMarksStocks(token, bookMarkId)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        getErrorString(
                            response.errorBody()
                                ?.parseError<CreateCategoryError>()?.response?.message?.getMessage()!!
                        )
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

    override suspend fun orderStock(
        token: String,
        bookMarkAvgKyat: MultipartBody.Part?,
        bookMarkAvgPae: MultipartBody.Part?,
        bookMarkAvgYwae: MultipartBody.Part?,
        goldQuality: MultipartBody.Part,
        goldSmith: MultipartBody.Part,
        bookMarkId: MultipartBody.Part,
        equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        jewellery_type_size_id: List<MultipartBody.Part>,
        order_qty: List<MultipartBody.Part>,
        sample_id: List<MultipartBody.Part>
    ): SimpleResponse {
        val response = orderStockService.orderStock(
            token,
            bookMarkAvgKyat,
            bookMarkAvgPae,
            bookMarkAvgYwae,
            goldQuality,
            goldSmith,
            bookMarkId,
            equivalent_pure_gold_weight_kpy,
            jewellery_type_size_id,
            order_qty,
            sample_id
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        getErrorString(
                            response.errorBody()
                                ?.parseError<CreateCategoryError>()?.response?.message?.getMessage()!!
                        )
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }
}