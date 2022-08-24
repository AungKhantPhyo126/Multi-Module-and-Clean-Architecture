package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JewelleryCategoryUiModel(
    val id:String,
    val name:String,
    val imageUrl:String,
    var isChecked:Boolean,
    var isFrequentlyUse:Boolean
):Parcelable

fun JewelleryCategory.asUiModel():JewelleryCategoryUiModel{
    return JewelleryCategoryUiModel(
        id = id,
        name=name,
        imageUrl = fileList[0].url,
        isChecked = false,
        isFrequentlyUse = isFrequentlyUse != "0"
    )
}
