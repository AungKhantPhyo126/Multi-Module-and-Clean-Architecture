package com.critx.data.network.datasource

import com.critx.data.datasource.dailyGoldAndPrice.DailyGoldAndPriceNetWorkDataSource
import com.critx.data.network.api.DailyGoldPriceService
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceDto
import javax.inject.Inject

class DailyGoldAndPriceDataSourceImpl @Inject constructor(
    private val dailyGoldPriceService: DailyGoldPriceService
):DailyGoldAndPriceNetWorkDataSource {
    override suspend fun getGoldPrice(token: String): List<GoldPriceDto> {
        val response = dailyGoldPriceService.getGoldPrice(token)
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
}