package com.critx.data.network.dto.setupStock

import com.critx.domain.model.SetupStock.ProductCodeDomain

data class ProductCodeResponse(
    val data:ProductCodeData
)
data class ProductCodeData(
    val code:String
)

fun ProductCodeData.asDomain():ProductCodeDomain{
    return ProductCodeDomain(
        code = code
    )
}