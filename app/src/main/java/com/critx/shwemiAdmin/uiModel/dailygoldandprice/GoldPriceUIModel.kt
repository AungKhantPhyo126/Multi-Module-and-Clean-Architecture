package com.critx.shwemiAdmin.uiModel.dailygoldandprice

import com.critx.domain.model.dailyGoldAndPrice.GoldPriceDomain

data class GoldPriceUIModel(
    val id:String,
    val name:String,
    val price:String,
    val unit:String
)

fun GoldPriceDomain.asUiModel():GoldPriceUIModel{
    return GoldPriceUIModel(
        id, name, price, unit
    )
}
