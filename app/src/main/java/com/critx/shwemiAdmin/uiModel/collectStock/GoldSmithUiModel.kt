package com.critx.shwemiAdmin.uiModel.collectStock

import com.critx.domain.model.collectStock.GoldSmithListDomain

data class GoldSmithUiModel(
    val id:String,
    val name:String
)

fun GoldSmithListDomain.asUiModel():GoldSmithUiModel{
    return GoldSmithUiModel(
        id = id,
        name = name
    )
}
