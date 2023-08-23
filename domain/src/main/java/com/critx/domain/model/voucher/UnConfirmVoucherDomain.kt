package com.critx.domain.model.voucher

data class UnConfirmVoucherDomain(
    val code: String?,
    val paid_amount: String?,
    val remaining_amount: String?,
    val cost:String?,
    val type: String?,
)