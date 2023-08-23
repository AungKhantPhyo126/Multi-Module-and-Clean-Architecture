package com.critx.data.network.dto

import com.critx.domain.model.voucher.UnConfirmVoucherDomain

data class UnConfirmVoucherResponse(
    val data: List<UnConfirmVoucherDto>
)

data class UnConfirmVoucherDto(
    val code: String?,
    val paid_amount: String?,
    val remaining_amount: String?,
    val cost:String?,
    val type: String?,
)

fun UnConfirmVoucherDto.asDomain():UnConfirmVoucherDomain{
    return UnConfirmVoucherDomain(
        code, paid_amount, remaining_amount, cost,type
    )
}
