package com.critx.shwemiAdmin.uiModel.checkupBoxWeight

import com.critx.domain.model.box.BoxWeightDomain

data class BoxWeightUiModel(
    val id:String,
    val code:String,
    val weight:String
)

fun BoxWeightDomain.asUiModel():BoxWeightUiModel{
    return BoxWeightUiModel(
        id, code, weight
    )
}
