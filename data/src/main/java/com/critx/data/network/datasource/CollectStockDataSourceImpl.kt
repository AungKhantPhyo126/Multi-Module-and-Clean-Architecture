package com.critx.data.network.datasource

import com.critx.data.datasource.collectStock.CollectStockDataSource
import com.critx.data.network.api.CollectStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.collectStock.GoldSmithListDto
import com.critx.data.network.dto.collectStock.JewellerySizeDto
import com.critx.data.network.dto.collectStock.ProductIdWithType
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import okhttp3.RequestBody
import javax.inject.Inject

class CollectStockDataSourceImpl @Inject constructor(
    private val collectStockService: CollectStockService
) : CollectStockDataSource {
    override suspend fun getProductId(token: String, productCode: String): String {
        val response = collectStockService.getProductId(token, productCode)
        return if (response.isSuccessful) {
            response.body()?.data?.id ?: throw Exception("Response body Null")
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

    override suspend fun collectSingle(
        token: String,
        productCode: String,
        weight: RequestBody
    ): String {
        val response = collectStockService.singleStockUpdate(token, productCode, weight)
        return if (response.isSuccessful) {
            response.body()?.response?.message ?: throw Exception("Response body Null")
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

//    override suspend fun getProductIdList(
//        token: String,
//        productCodeList: List<RequestBody>
//    ): List<String> {
//        val response = collectStockService.getProductIdList(token, productCodeList)
//        return if (response.isSuccessful) {
//            response.body()?.data ?: throw Exception("Response body Null")
//        } else {
//            throw  Exception(
//                when (response.code()) {
//                    400 -> {
//                        response.errorBody()?.string()?:"Bad request"
//                    }
//                    401 -> "You are not Authorized"
//                    402 -> "Payment required!!!"
//                    403 -> "Forbidden"
//                    404 -> "You request not found"
//                    405 -> "Method is not allowed!!!"
//                    else -> "Unhandled error occurred!!!"
//                }
//            )
//        }
//    }

    override suspend fun scanProductCode(
        token: String,
        productCode: String
    ): ProductIdWithType {
        val response = collectStockService.scanStockCode(token, productCode)
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

    override suspend fun getJewellerySize(token: String, type: String): List<JewellerySizeDto> {
        val response = collectStockService.getJewellerySize(token, type)
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

    override suspend fun getGoldSmithList(token: String, type: String): List<GoldSmithListDto> {
        val response = collectStockService.getGoldSmitList(token, type)
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

    override suspend fun collectBatch(
        token: String,
        method: RequestBody,
        ywae: RequestBody?,
        goldSmithId: RequestBody?,
        bonus: RequestBody?,
        jewellerySizeId: RequestBody?,
        productIds: List<RequestBody>
    ): SimpleResponse {
        val response = collectStockService.multipleStockUpdate(
            token,
            method,
            ywae,
            goldSmithId,
            bonus,
            jewellerySizeId,
            productIds
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