package com.critx.shwemiAdmin.uiModel.collectStock

import com.critx.domain.model.collectStock.JewellerySizeDomain
import com.critx.domain.model.orderStock.BookMarkStockInfoDomain
import com.critx.shwemiAdmin.uiModel.orderStock.BookMarkStockUiModel

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

fun JewellerySizeUIModel.asBookMarkOrder(): BookMarkStockInfoDomain {
    return BookMarkStockInfoDomain(
        id = id,
        size = quantity,
        stock =""
    )
}
