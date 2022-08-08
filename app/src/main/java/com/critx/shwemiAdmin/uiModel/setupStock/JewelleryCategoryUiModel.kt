package com.critx.shwemiAdmin.uiModel.setupStock

import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory

data class JewelleryCategoryUiModel(
    val id:String,
    val name:String,
    val imageUrl:String,
    var isChecked:Boolean,
    var isFrequentlyUse:Boolean
)

fun JewelleryCategory.asUiModel():JewelleryCategoryUiModel{
    return JewelleryCategoryUiModel(
        id = id,
        name=name,
        imageUrl = fileList[0].url,
        isChecked = false,
        isFrequentlyUse = isFrequentlyUse != "0"
    )
}
