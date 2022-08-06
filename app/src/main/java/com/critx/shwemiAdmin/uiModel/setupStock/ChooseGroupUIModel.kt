package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChooseGroupUIModel(
    val id:String,
    val name:String,
    val imageUrl:String,
    var isChecked:Boolean,
    var isFrequentlyUse:Boolean
):Parcelable
fun JewelleryGroup.asUiModel():ChooseGroupUIModel{
    return ChooseGroupUIModel(
        id = id,
        name = name,
        imageUrl = image,
        isChecked = false,
        isFrequentlyUse = isFrequentlyUse != "0"
    )
}
