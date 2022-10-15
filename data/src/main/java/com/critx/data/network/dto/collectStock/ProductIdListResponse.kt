package com.critx.data.network.dto.collectStock

import com.critx.domain.model.collectStock.ProductIdWithTypeDomain

data class ProductIdListResponse(
    val data:ProductIdWithType
)

data class ProductIdWithType(
    val id:String,
    val jewellery_type_id:Int
)

fun ProductIdWithType.asDomain():ProductIdWithTypeDomain{
    return ProductIdWithTypeDomain(
        id = id,
        type = jewellery_type_id
    )
}
