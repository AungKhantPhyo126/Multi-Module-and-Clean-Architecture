package com.critx.data.datasource.voucher

import com.critx.data.network.dto.DiscountVoucherScanDto
import com.critx.data.network.dto.ScanVoucherToConfirmDto
import com.critx.data.network.dto.ScanVoucherToConfirmResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.StockInVoucherDto
import com.critx.data.network.dto.UnConfirmVoucherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ConfirmVoucherDataSource {

    suspend fun getVouchers(
        token: String,
        type: String,
    ): List<UnConfirmVoucherDto>

    suspend fun getStockInVoucher(
        token: String,
        voucherCode: String,
    ): List<StockInVoucherDto>

    suspend fun confirmVoucher(
        token: String,
        voucherCode: String,
    ): String

    suspend fun scanDiscountVoucher(
        token: String,
        voucherCode: String,
    ): DiscountVoucherScanDto

    suspend fun scanVoucherToConfirm(
        token: String,
        voucherCode: String,
    ): ScanVoucherToConfirmDto

    suspend fun addDiscount(
        token: String,
        voucherCodes:List<String>,
        amount:String,
    ):SimpleResponseDto
}