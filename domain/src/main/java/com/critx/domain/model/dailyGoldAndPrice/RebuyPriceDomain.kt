package com.critx.domain.model.dailyGoldAndPrice
data class RebuyPriceDomain(
    val horizontalOptionLevel: String,
    val verticalOptionLevel: String,
    val price:String
)

data class RebuyPriceSmallAndLargeDomain(
    val small:List<RebuyPriceDomain>,
    val large:List<RebuyPriceDomain>
)
