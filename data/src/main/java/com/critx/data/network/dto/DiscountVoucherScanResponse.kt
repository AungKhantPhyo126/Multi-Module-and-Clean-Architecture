package com.critx.data.network.dto

import com.critx.domain.model.DiscountVoucherScanDomain

data class DiscountVoucherScanResponse(
   val data:DiscountVoucherScanDto
)

data class DiscountVoucherScanDto(
    val id:String,
    val code:String
)

fun DiscountVoucherScanDto.asDomain():DiscountVoucherScanDomain{
    return DiscountVoucherScanDomain(id,code)
}