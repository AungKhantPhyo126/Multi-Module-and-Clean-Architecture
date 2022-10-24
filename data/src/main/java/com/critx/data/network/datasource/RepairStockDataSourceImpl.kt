package com.critx.data.network.datasource

import com.critx.data.datasource.repairStock.RepairStockDataSource
import com.critx.data.network.api.RepairStockService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.repairStock.JobDoneResponse
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.getMessage
import okhttp3.RequestBody

class RepairStockDataSourceImpl constructor(
    private val repairStockService: RepairStockService
) : RepairStockDataSource {
    override suspend fun getJobDoneData(token: String, goldSmithId: String): JobDoneResponse {
        val response = repairStockService.getJobDoneData(token, goldSmithId)
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

    override suspend fun assignGoldSmith(
        token: String,
        goldSmithId: RequestBody,
        jewelleryTypeId: RequestBody,
        repairJobId: RequestBody,
        quantity: RequestBody,
        weightGm: RequestBody
    ): SimpleResponseDto {
        val response = repairStockService.assignGoldSmith(
            token,
            goldSmithId,
            jewelleryTypeId,
            repairJobId,
            quantity,
            weightGm
        )
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
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

    override suspend fun chargeRepairSTock(
        token: String,
        goldSmithId: RequestBody,
        repairStockList: List<RequestBody>
    ): SimpleResponseDto {
        val response = repairStockService.chargeRepairSTock(token, goldSmithId, repairStockList)
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
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