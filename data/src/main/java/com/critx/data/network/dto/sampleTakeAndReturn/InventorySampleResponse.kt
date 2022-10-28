package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.InventorySampleDomain
import com.critx.domain.model.sampleTakeAndReturn.ProductDomain

data class InventorySampleResponse(
    val data : List<InventorySampleDto>
)

data class InventorySampleDto(
    val file: FileShweMiDto,
    val id: Int,
    val product: ProductDto,
    val specification: String,
    val type: Int
)
data class ProductDto(
    val code: String,
    val id: String
)

fun ProductDto.asDomain():ProductDomain{
    return ProductDomain(code, id)
}

fun InventorySampleDto.asDomain():InventorySampleDomain{
    return InventorySampleDomain(
        file.asDomain(),
        id,product.asDomain(),specification, type
    )
}