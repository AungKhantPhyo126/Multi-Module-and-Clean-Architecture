package com.critx.data.network.datasource

import com.critx.data.datasource.dailyGoldAndPrice.DailyGoldAndPriceNetWorkDataSource
import com.critx.data.network.api.DailyGoldPriceService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceDto
import com.critx.data.network.dto.dailygoldAndPrice.RebuyPriceDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import javax.inject.Inject

class DailyGoldAndPriceDataSourceImpl @Inject constructor(
    private val dailyGoldPriceService: DailyGoldPriceService
) : DailyGoldAndPriceNetWorkDataSource {
    override suspend fun getGoldPrice(token: String): List<GoldPriceDto> {
        val response = dailyGoldPriceService.getGoldPrice(token)
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

    override suspend fun updateGoldPrice(
        token: String,
        price: HashMap<String, String>
    ): SimpleResponseDto {
        val response = dailyGoldPriceService.updateGoldPrice(token, price)
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
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

    override suspend fun getRebuyPrice(token: String): RebuyPriceDto {
        val response = dailyGoldPriceService.getRebuyGoldPrice(token)
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

    override suspend fun updateRebuyPrice(
        token: String,
        horizontal_option_name: HashMap<String, String>,
        vertical_option_name: HashMap<String, String>,
        horizontal_option_level: HashMap<String, String>,
        vertical_option_level: HashMap<String, String>,
        size: HashMap<String, String>,
        price: HashMap<String, String>
    ):SimpleResponseDto {
        val response = dailyGoldPriceService.updateRebuyPrice(
            token,
            horizontal_option_name,
            vertical_option_name,
            horizontal_option_level,
            vertical_option_level,
            size,
            price
        )
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
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