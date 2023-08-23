package com.critx.data.network.datasource

import com.critx.data.datasource.giveGold.GiveGoldDataSource
import com.critx.data.network.api.GiveGoldService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.SimpleResponseWithStringData
import com.critx.data.network.dto.giveGold.GiveGoldScanDto
import com.critx.data.network.dto.giveGold.GoldBoxDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import retrofit2.Response
import javax.inject.Inject

class GiveGoldDataSourceImpl @Inject constructor(
    private val giveGoldService: GiveGoldService
) : GiveGoldDataSource {
    override suspend fun giveGold(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight: String,
        wastageY: String,
        dueDate: String?,
        sampleList: List<String>?
    ): String {
        val response = giveGoldService.giveGold(
            token, goldSmithId, orderItem, orderQty, weightY,
            goldBoxId, goldWeight, gemWeight, goldAndGemWeight, wastageY, dueDate, sampleList
        )
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

    override suspend fun getGoldBoxId(token: String): List<GoldBoxDto> {
        val response = giveGoldService.getGoldBoxId(token)
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

    override suspend fun serviceCharge(
        token: String,
        chargeAmount: String,
        wastageGm: String,
        invoice: String
    ): SimpleResponseDto {
        val response = giveGoldService.serviceCharge(token, chargeAmount, wastageGm, invoice)
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

    override suspend fun giveGoldScan(token: String, invoiceNumber: String): GiveGoldScanDto {
        val response = giveGoldService.giveGoldScan(token, invoiceNumber)
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

    override suspend fun getPdfPrint(token: String, voucherID: String): String {
        val response = giveGoldService.getPdfPrint(token, voucherID)
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