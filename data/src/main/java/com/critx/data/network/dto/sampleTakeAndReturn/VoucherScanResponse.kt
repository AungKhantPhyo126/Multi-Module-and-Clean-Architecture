package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain

data class VoucherScanResponse(
    val data:VoucherScanDto
)

data class VoucherScanDto(
    val id:String?,
    val code:String?
)

fun VoucherScanDto.asDomain():VoucherScanDomain{
    return VoucherScanDomain(
        id = id.orEmpty(),
        code = code.orEmpty()
    )
}
