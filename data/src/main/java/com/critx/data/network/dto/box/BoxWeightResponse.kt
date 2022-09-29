package com.critx.data.network.dto.box

import com.critx.domain.model.box.BoxWeightDomain

data class BoxWeightResponse(
    val data:List<BoxWeightDto>
)
data class BoxWeightDto(
    val id:String?,
    val code:String?,
    val weight:String?
)

fun BoxWeightDto.asDomain():BoxWeightDomain{
    return BoxWeightDomain(
        id=id.orEmpty(),
        code=code.orEmpty(),
        weight =weight.orEmpty()
    )
}
