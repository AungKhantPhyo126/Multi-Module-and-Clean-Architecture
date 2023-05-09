package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.CustomerIdDomain
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.domain.model.voucher.ScanVoucherToConfirmDomain
import kotlinx.coroutines.flow.Flow

interface ConfirmVoucherRepository {

    fun getVouchers(
        token: String,
        type: String,
    ): Flow<Resource<String>>

    fun getStockInVoucher(
        token: String,
        voucherCode: String,
    ): Flow<Resource<String>>

    fun confirmVoucher(
        token: String,
        voucherCode: String,
    ): Flow<Resource<String>>
    fun scanDiscountVoucher(
        token: String,
        voucherCode: String
    ): Flow<Resource<DiscountVoucherScanDomain>>

    fun scanVoucherToConfirm(
        token: String,
        voucherCode: String,
    ): Flow<Resource<ScanVoucherToConfirmDomain>>
}