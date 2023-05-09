package com.critx.data.network.datasource

import com.critx.data.datasource.voucher.ConfirmVoucherDataSource
import com.critx.data.network.api.ConfirmVoucherService
import com.critx.data.network.dto.DiscountVoucherScanDto
import com.critx.data.network.dto.ScanVoucherToConfirmDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import javax.inject.Inject

class ConfirmVoucherDataSourceImpl @Inject constructor(
    private val confirmVoucherService: ConfirmVoucherService
) : ConfirmVoucherDataSource {
    override suspend fun getVouchers(token: String, type: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getStockInVoucher(token: String, voucherCode: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun confirmVoucher(token: String, voucherCode: String): String {
        val response = confirmVoucherService.confirmVoucher(
            token,
            voucherCode
        )
        return if (response.isSuccessful) {
            response.body()?.response?.message ?: throw Exception("Response body Null")
        } else {
            throw Exception(
                when (response.code()) {
                    400 -> {
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

    override suspend fun scanDiscountVoucher(
        token: String,
        voucherCode: String
    ): DiscountVoucherScanDto {
        val response = confirmVoucherService.scanDiscountVoucher(
            token,
            voucherCode
        )
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw Exception(
                when (response.code()) {
                    400 -> {
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

    override suspend fun scanVoucherToConfirm(
        token: String,
        voucherCode: String
    ): ScanVoucherToConfirmDto {
        val response = confirmVoucherService.scanVoucherToConfirm(
            token,
            voucherCode
        )
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw Exception(
                when (response.code()) {
                    400 -> {
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