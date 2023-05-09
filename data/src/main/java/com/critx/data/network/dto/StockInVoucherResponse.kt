package com.critx.data.network.dto

import com.critx.domain.model.StockInVoucherDomain

data class StockInVoucherResponse(
    val data: List<StockInVoucherDto>
)

data class StockInVoucherDto(
    val code: String?,
    val gold_weight_diff_ywae: String?,
    val reason: String?
)

fun StockInVoucherDto.asDomain():StockInVoucherDomain{
    return StockInVoucherDomain(
        code, gold_weight_diff_ywae, reason
    )
}

