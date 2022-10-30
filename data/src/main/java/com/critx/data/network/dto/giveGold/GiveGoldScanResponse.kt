package com.critx.data.network.dto.giveGold

import com.critx.domain.model.giveGold.GiveGoldScanDomain

data class GiveGoldScanResponse(
    val data:GiveGoldScanDto
)

data class GiveGoldScanDto(
    val id:String
)

fun GiveGoldScanDto.asDomain():GiveGoldScanDomain{
    return GiveGoldScanDomain(
        id
    )
}
