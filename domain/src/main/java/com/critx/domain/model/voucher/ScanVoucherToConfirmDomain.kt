package com.critx.domain.model.voucher

data class ScanVoucherToConfirmDomain(
    val code: String,
    val total_cost: String?,
    val old_voucher_paid_amount: String?,
    val paid_amount: String?,
    val remaining_amount: String?,
    val gold_gem_weight_ywae: String?,
    val type: String?
)
