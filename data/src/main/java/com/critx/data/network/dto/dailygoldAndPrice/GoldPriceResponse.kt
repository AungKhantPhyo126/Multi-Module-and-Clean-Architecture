package com.critx.data.network.dto.dailygoldAndPrice

import com.critx.domain.model.dailyGoldAndPrice.GoldPriceDomain

data class GoldPriceResponse(
    val data:List<GoldPriceDto>
)

data class GoldPriceDto(
    val id:String?,
    val name:String?,
    val price:String?,
    val per_unit:String?
)

fun GoldPriceDto.asDomain():GoldPriceDomain{
    return GoldPriceDomain(
        id =id.orEmpty(),
        name = name.orEmpty(),
        price = price.orEmpty(),
        unit = per_unit.orEmpty()
    )
}
