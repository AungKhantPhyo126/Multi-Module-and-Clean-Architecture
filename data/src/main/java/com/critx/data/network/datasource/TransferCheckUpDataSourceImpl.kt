package com.critx.data.network.datasource

import com.critx.data.datasource.transferCheckUp.TransferCheckUpNetWorkDataSource
import com.critx.data.network.api.TransferCheckUpService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.network.dto.transferCheckUp.CheckUpDto
import com.critx.data.parseError
import com.critx.data.parseErrorSingle
import com.critx.data.parseErrorWithDataClass
import javax.inject.Inject

class TransferCheckUpDataSourceImpl @Inject constructor(
    private val transferCheckUpService: TransferCheckUpService
) : TransferCheckUpNetWorkDataSource {
    override suspend fun checkUp(
        token: String,
        boxCode: String,
        productIdList: List<String>
    ): CheckUpDto {
        val response = transferCheckUpService.checkUp(token, boxCode, productIdList)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                       val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()?.parseErrorWithDataClass<SimpleError>(errorJsonString)
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

    override suspend fun transfer(
        token: String,
        boxCode: String,
        productIdList: List<String>,
        rfidCode: HashMap<String, String>
    ): SimpleResponseDto {
        val response = transferCheckUpService.transfer(token, boxCode, productIdList, rfidCode)
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                           val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()?.parseErrorWithDataClass<SimpleError>(errorJsonString)
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