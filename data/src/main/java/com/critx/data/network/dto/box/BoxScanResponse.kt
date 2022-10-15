package com.critx.data.network.dto.box

import com.critx.domain.model.box.BoxScanDomain

data class BoxScanResponse(
    val data:BoxScanDto
)

data class BoxScanDto(
    val id:String?,
    val code:String?,
    val qty:String?,
    val jewellery_type:String?
)

fun BoxScanDto.asDomain():BoxScanDomain{
    return  BoxScanDomain(
        id = id.orEmpty(),
        code = code.orEmpty(),
        qty = qty.orEmpty(),
        jewellery_type = jewellery_type.orEmpty()
    )
}
