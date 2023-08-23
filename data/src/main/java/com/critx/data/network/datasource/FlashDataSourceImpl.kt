package com.critx.data.network.datasource

import com.critx.data.datasource.flashSale.FlashDataSource
import com.critx.data.network.api.FlashSaleService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.flashSales.CustomerIdDto
import com.critx.data.network.dto.flashSales.UserPointsDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class FlashDataSourceImpl @Inject constructor(
    private val flashSaleService: FlashSaleService
) : FlashDataSource {
    override suspend fun addToFlashSale(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
        productIds: List<RequestBody>,
        image: MultipartBody.Part
    ): SimpleResponseDto {
        val response = flashSaleService.addToFlashSale(
            token,
            title,
            discount_amount,
            time_from,
            time_to,
            productIds,
            image
        )
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw Exception(
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

    override suspend fun getUserPoint(
        token: String, userCode: String,
    ): UserPointsDto {
        val response = flashSaleService.getUserPoint(token,userCode)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw Exception(
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

    override suspend fun manualPointsAddOrReduce(
        token: String,
        user_id:RequestBody,
        point:RequestBody,
        reason:RequestBody,
        action:RequestBody,
    ): SimpleResponseDto {
        val response = flashSaleService.manualPointsAddOrReduce(
            token,
           user_id, point, reason, action
        )
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw Exception(
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

    override suspend fun userScan(token: String, userCode: String): CustomerIdDto {
        val response = flashSaleService.userScan(token, userCode)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw Exception(
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