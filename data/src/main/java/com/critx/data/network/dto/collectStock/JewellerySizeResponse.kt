package com.critx.data.network.dto.collectStock

import com.critx.domain.model.collectStock.JewellerySizeDomain

data class JewellerySizeResponse(
    val data:List<JewellerySizeDto>
)

data class JewellerySizeDto(
    val id:String?,
    val quantity:String?
)

fun JewellerySizeDto.asDomain():JewellerySizeDomain{
    return JewellerySizeDomain(
        id = id.orEmpty(),
        quantity = quantity.orEmpty()
    )
}