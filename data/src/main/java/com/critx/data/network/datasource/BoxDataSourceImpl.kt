package com.critx.data.network.datasource

import com.critx.data.datasource.box.BoxNetWorkDataSource
import com.critx.data.network.api.BoxService
import com.critx.data.network.dto.box.BoxScanDto
import com.critx.data.network.dto.box.BoxWeightDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.getMessage
import javax.inject.Inject

class BoxDataSourceImpl @Inject constructor(
    private val boxService: BoxService
) :BoxNetWorkDataSource{
    override suspend fun getBoxWeight(token: String, boxIdList: List<String>): List<BoxWeightDto> {
        val response = boxService.getBoxWeight(token,boxIdList)
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

    override suspend fun getBoxData(token: String, boxCode: String): BoxScanDto {
        val response = boxService.getBoxData(token,boxCode)
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
}