package com.critx.data.network.datasource

import com.critx.data.datasource.giveGold.GiveGoldDataSource
import com.critx.data.network.api.GiveGoldService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.giveGold.GiveGoldScanDto
import com.critx.data.network.dto.giveGold.GoldBoxDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.getMessage
import javax.inject.Inject

class GiveGoldDataSourceImpl @Inject constructor(
    private val giveGoldService: GiveGoldService
) : GiveGoldDataSource {
    override suspend fun giveGold(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightK: String,
        weighP: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight:String,
        wastageK: String,
        wastageP: String,
        wastageY: String,
        dueDate: String,
        sampleList: List<String>?
    ): SimpleResponseDto {
        val response = giveGoldService.giveGold(
            token, goldSmithId, orderItem, orderQty, weightK, weighP, weightY,
            goldBoxId, goldWeight, gemWeight,goldAndGemWeight, wastageK, wastageP, wastageY, dueDate, sampleList
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

    override suspend fun getGoldBoxId(token: String): List<GoldBoxDto> {
        val response = giveGoldService.getGoldBoxId(token)
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

    override suspend fun giveGoldScan(token: String, invoiceNumber: String): GiveGoldScanDto {
        val response = giveGoldService.giveGoldScan(token, invoiceNumber)
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
        }    }
}