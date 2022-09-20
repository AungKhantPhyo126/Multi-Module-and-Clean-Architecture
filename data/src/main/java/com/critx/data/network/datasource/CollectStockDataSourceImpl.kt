package com.critx.data.network.datasource

import com.critx.data.datasource.collectStock.CollectStockDataSource
import com.critx.data.network.api.CollectStockService
import com.critx.data.network.dto.SimpleResponse
import okhttp3.RequestBody
import javax.inject.Inject

class CollectStockDataSourceImpl @Inject constructor(
    private val collectStockService: CollectStockService
):CollectStockDataSource {
    override suspend fun getProductId(token:String, productCode: String): String {
        val response = collectStockService.getProductId(token, productCode)
        return if (response.isSuccessful) {
            response.body()?.data?.id ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun collectSingle(token: String, productCode: String,weight:RequestBody): String {
        val response = collectStockService.singleStockUpdate(token, productCode,weight)
        return if (response.isSuccessful) {
            response.body()?.response?.message ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun getProductIdList(
        token: String,
        productCodeList: List<RequestBody>
    ): List<String> {
        val response = collectStockService.getProductIdList(token, productCodeList)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string()?:"Bad request"
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