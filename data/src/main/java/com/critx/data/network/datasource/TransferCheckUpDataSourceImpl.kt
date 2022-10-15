package com.critx.data.network.datasource

import com.critx.data.datasource.transferCheckUp.TransferCheckUpNetWorkDataSource
import com.critx.data.network.api.TransferCheckUpService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.transferCheckUp.CheckUpDto
import javax.inject.Inject

class TransferCheckUpDataSourceImpl @Inject constructor(
    private val transferCheckUpService: TransferCheckUpService
) : TransferCheckUpNetWorkDataSource {
    override suspend fun checkUp(
        token: String,
        boxCode: String,
        productIdList: List<String>
    ): List<CheckUpDto> {
        val response = transferCheckUpService.checkUp(token, boxCode, productIdList)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string() ?: "Bad request"
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
                        response.errorBody()?.string() ?: "Bad request"
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