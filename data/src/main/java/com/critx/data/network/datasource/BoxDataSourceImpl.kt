package com.critx.data.network.datasource

import com.critx.data.datasource.box.BoxNetWorkDataSource
import com.critx.data.network.api.BoxService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.box.BoxScanDto
import com.critx.data.network.dto.box.BoxWeightDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import javax.inject.Inject

class BoxDataSourceImpl @Inject constructor(
    private val boxService: BoxService
) : BoxNetWorkDataSource {
    override suspend fun getBoxWeight(token: String, boxIdList: List<String>): List<BoxWeightDto> {
        val response = boxService.getBoxWeight(token, boxIdList)
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

    override suspend fun getBoxData(token: String, boxCode: String): BoxScanDto {
        val response = boxService.getBoxData(token, boxCode)
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

    override suspend fun arrangeBox(token: String, boxes: List<String>): SimpleResponseDto {
        val response = boxService.arrangeBox(token, boxes)
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
}