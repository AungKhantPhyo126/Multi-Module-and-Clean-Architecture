package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JewelleryCategoryUiModel(
    val id:String,
    val name:String,
    val imageUrlList:List<String>,
    val video:String?,
    var isChecked:Boolean,
    var isFrequentlyUse:Boolean,
    val specification : String?,
    val avgWeightPerUnitGm:Double?,
    val avgWastagePerUnitKpy:Double?,
):Parcelable

fun JewelleryCategory.asUiModel():JewelleryCategoryUiModel{
    return JewelleryCategoryUiModel(
        id = id,
        name=name,
        imageUrlList = fileList.filter { it.type == "image" }.map { it.url },
        isChecked = false,
        isFrequentlyUse = isFrequentlyUse != "0",
        specification = specification,
        avgWeightPerUnitGm =avgWeightPerUnitGm,
        avgWastagePerUnitKpy =avgWastagePerUnitKpy,
        video = fileList.find { it.type == "video" }?.url
    )
}
