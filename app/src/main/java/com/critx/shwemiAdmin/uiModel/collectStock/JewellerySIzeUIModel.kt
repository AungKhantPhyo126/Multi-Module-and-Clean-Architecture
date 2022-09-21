package com.critx.shwemiAdmin.uiModel.collectStock

import com.critx.domain.model.collectStock.JewellerySizeDomain

data class JewellerySizeUIModel(
    val id:String,
    val quantity:String,
    var isChecked:Boolean
)
fun JewellerySizeDomain.asUiModel():JewellerySizeUIModel{
    return JewellerySizeUIModel(
        id = id,
        quantity = quantity,
        isChecked = false
    )
}
