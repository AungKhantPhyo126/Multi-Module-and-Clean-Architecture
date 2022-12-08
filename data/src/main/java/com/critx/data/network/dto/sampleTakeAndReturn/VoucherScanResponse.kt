package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherTypeDomain

data class VoucherScanResponse(
    val data:VoucherScanDto
)

data class VoucherScanDto(
    val id:String?,
    val code:String?,
    val qty:String?,

)
data class VoucherTypeDto(
    val name:String,
    val count:String
)

fun VoucherScanDto.asDomain():VoucherScanDomain{
    return VoucherScanDomain(
        id = id.orEmpty(),
        code = code.orEmpty(),
        qty = qty.orEmpty(),
    )
}

fun VoucherTypeDto.asDomain():VoucherTypeDomain{
    return VoucherTypeDomain(
        name, count
    )
}
