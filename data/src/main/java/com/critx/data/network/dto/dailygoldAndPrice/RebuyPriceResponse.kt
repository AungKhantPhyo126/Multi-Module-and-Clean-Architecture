package com.critx.data.network.dto.dailygoldAndPrice

import com.critx.domain.model.dailyGoldAndPrice.RebuyPriceDomain
import com.critx.domain.model.dailyGoldAndPrice.RebuyPriceSmallAndLargeDomain

data class RebuyPriceResponse(
    val data:RebuyPriceDto
)


data class RebuyPriceDto(
    val small_rebuy_prices:List<RebuyPrice>,
    val large_rebuy_prices:List<RebuyPrice>,

)

data class RebuyPrice(
    val horizontal_option_level: String,
    val vertical_option_level: String,
    val price:String
)

fun RebuyPriceDto.asDomain():RebuyPriceSmallAndLargeDomain{
    return RebuyPriceSmallAndLargeDomain(
        small = small_rebuy_prices.map { it.asDomain() },
        large = large_rebuy_prices.map { it.asDomain() },
    )
}

fun RebuyPrice.asDomain():RebuyPriceDomain{
    return RebuyPriceDomain(
        horizontalOptionLevel = horizontal_option_level,
        verticalOptionLevel = vertical_option_level,
        price = price
    )
}