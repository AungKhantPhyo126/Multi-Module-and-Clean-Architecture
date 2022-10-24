package com.critx.data.network.dto.giveGold

import com.critx.domain.model.giveGold.GoldBoxDomain

data class GoldBoxResponse(
    val data:List<GoldBoxDto>
)

data class GoldBoxDto(
    val id:String,
    val name:String
)

fun GoldBoxDto.asDomain():GoldBoxDomain{
    return GoldBoxDomain(
        id, name
    )
}