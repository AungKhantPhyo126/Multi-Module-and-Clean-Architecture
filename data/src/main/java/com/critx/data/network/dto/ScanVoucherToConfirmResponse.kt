package com.critx.data.network.dto

import com.critx.domain.model.voucher.ScanVoucherToConfirmDomain

data class ScanVoucherToConfirmResponse(
    val data: ScanVoucherToConfirmDto
)

data class ScanVoucherToConfirmDto(
    val code: String,
    val total_cost: String?,
    val old_voucher_paid_amount: String?,
    val paid_amount: String?,
    val remaining_amount: String?,
    val gold_gem_weight_ywae: String?,
    val type: String?
)

fun ScanVoucherToConfirmDto.asDomain():ScanVoucherToConfirmDomain{
    return ScanVoucherToConfirmDomain(
        code, total_cost, old_voucher_paid_amount, paid_amount, remaining_amount, gold_gem_weight_ywae, type
    )
}

