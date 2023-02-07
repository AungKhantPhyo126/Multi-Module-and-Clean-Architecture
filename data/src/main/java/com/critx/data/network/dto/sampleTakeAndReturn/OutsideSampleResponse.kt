package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.OutsideSampleDomain
import com.critx.domain.model.sampleTakeAndReturn.ProductDomain

data class OutsideSampleResponse(
    val data : List<OutsideSampleDto>
)

data class OutsideSampleDto(
    val file: FileShweMiDto,
    val id: Int,
    val name:String?,
    val weight_gm:String?,
    val specification: String?,
    val type: Int
)
data class ProductDto(
    val code: String,
    val id: String
)

fun ProductDto.asDomain():ProductDomain{
    return ProductDomain(code, id)
}

fun OutsideSampleDto.asDomain(): OutsideSampleDomain {
    return OutsideSampleDomain(
        file.asDomain(),
        id,name.orEmpty(),weight_gm.orEmpty(),specification.orEmpty(), type
    )
}