package com.critx.data.network.datasource

import com.critx.data.datasource.SampleTakeAndReturn.SampleTakeAndReturnNetWorkDataSource
import com.critx.data.network.api.SampleTakeAndReturnService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.SimpleResponseWithData
import com.critx.data.network.dto.sampleTakeAndReturn.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SampleTakeAndReturnDataSourceImpl @Inject constructor(
    private val sampleTakeAndReturnService: SampleTakeAndReturnService
):SampleTakeAndReturnNetWorkDataSource {
    override suspend fun scanInvoice(token: String, invoiceCode: String): VoucherScanDto {
        val response = sampleTakeAndReturnService.scanInvoice(token,invoiceCode)
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

    override suspend fun getVoucherSample(token: String, invoiceId: String): List<VoucherSampleDto> {
        val response = sampleTakeAndReturnService.getVoucherSamples(token,invoiceId)
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

    override suspend fun getOutsideSample(token: String): List<OutsideSampleDto> {
        val response = sampleTakeAndReturnService.getOutsideSample(token)
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

    override suspend fun getInventorySample(token: String): List<OutsideSampleDto> {
        val response = sampleTakeAndReturnService.getInventorySample(token)
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
        }    }


    override suspend fun checkSample(token: String, invoiceId: String): SampleCheckDto {
        val response = sampleTakeAndReturnService.checkSamples(token,invoiceId)
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

    override suspend fun saveSample(
        token: String,
        sample: HashMap<String, String>
    ): SimpleResponseDto {
        val response = sampleTakeAndReturnService.saveSample(token,sample)
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

    override suspend fun getHandedList(token: String): List<HandedListDto> {
        val response = sampleTakeAndReturnService.getHandedList(token)
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

    override suspend fun addToHandedList(token: String, sampleId: List<String>): SimpleResponseDto {
        val response = sampleTakeAndReturnService.addToHandedList(token,sampleId)
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

    override suspend fun removeFromHandedList(
        token: String,
        sampleId: String
    ): SimpleResponseDto {
        val response = sampleTakeAndReturnService.removeFromHandedList(token,sampleId)
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

    override suspend fun saveOutsideSample(
        token: String,
        name: RequestBody?,
        weight: RequestBody?,
        specification: RequestBody?,
        image: MultipartBody.Part
    ): SimpleResponseWithData {
        val response = sampleTakeAndReturnService.saveOutsideSample(token,name,weight,specification, image)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
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

    override suspend fun returnSample(token: String, sampleId: List<String>): SimpleResponseDto {
        val response = sampleTakeAndReturnService.returnSample(token,sampleId)
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