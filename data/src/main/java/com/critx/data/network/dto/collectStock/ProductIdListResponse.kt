package com.critx.data.network.dto.collectStock

import com.critx.domain.model.collectStock.ProductIdWithTypeDomain

data class ProductIdListResponse(
    val data:ProductIdWithType
)

data class ProductIdWithType(
    val id:String,
    val jewelleryType:Int
)

fun ProductIdWithType.asDomain():ProductIdWithTypeDomain{
    return ProductIdWithTypeDomain(
        id = id,
        type = jewelleryType
    )
}
