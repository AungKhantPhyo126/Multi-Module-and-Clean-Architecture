package com.critx.data.network.datasource

import com.critx.data.datasource.orderStock.OrderStockDataSource
import com.critx.data.network.api.OrderStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.orderStock.BookMarkStockInfoDto
import com.critx.data.network.dto.orderStock.BookMarkedStocksResponse
import com.critx.data.network.dto.orderStock.GsNewItemsResponse
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import okhttp3.MultipartBody
import javax.inject.Inject

class OrderStockDataSourceImpl @Inject constructor(
    private val orderStockService: OrderStockService
) : OrderStockDataSource {
    override suspend fun getBookMarkStockList(
        token: String,
        jewelleryType: String,
        isItemFromGs: String,
        page: Int
    ): BookMarkedStocksResponse {
        val response = orderStockService.getBookMarks(token, jewelleryType, isItemFromGs, page)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun getGsNewItems(token: String, page: Int,jewelleryType: String): GsNewItemsResponse {
        val response = orderStockService.getGsNewItems(token, page,jewelleryType)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun orderStock(
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
        custom_category_name:MultipartBody.Part?

    ): SimpleResponse {
        val response = orderStockService.orderStock(
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
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }
}