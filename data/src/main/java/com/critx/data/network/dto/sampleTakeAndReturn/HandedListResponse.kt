package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.HandedListDomain

data class HandedListResponse(
    val data:List<HandedListDto>
)

data class HandedListDto(
    val id:String?,
    val product_id:String?,
    val specification:String?,
    val name:String?,
    val weight_gm:String?,
    val type:String?,
    val file:FileShweMiDto?
)

fun HandedListDto.asDomain():HandedListDomain{
    return HandedListDomain(
        id= id.orEmpty(),
        product_id = product_id.orEmpty(),
        specification = specification.orEmpty(),
        name = name.orEmpty(),
        weight = weight_gm.orEmpty(),
        type = type.orEmpty(),
        file=file!!.asDomain()
    )
}
