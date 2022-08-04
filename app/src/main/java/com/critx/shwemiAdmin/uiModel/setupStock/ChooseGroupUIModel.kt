package com.critx.shwemiAdmin.uiModel.setupStock

import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup

data class ChooseGroupUIModel(
    val id:String,
    val name:String,
    val imageUrl:String,
    var isChecked:Boolean
)

fun JewelleryGroup.asUiModel():ChooseGroupUIModel{
    return ChooseGroupUIModel(
        id = id,
        name = name,
        imageUrl = image,
        isChecked = false
    )
}
