package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryCategory.AverageKPYDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JewelleryCategoryUiModel(
    val id:String,
    val name:String,
    val imageUrlList:List<String>,
    val video:String?,
    var isChecked:Boolean = false,
    var isFrequentlyUse:Boolean,
    var withGem:Boolean,
    var orderToGs:Boolean,
    val specification : String?,
    val avgWeightPerUnitGm:Double?,
    val avgWastagePerUnitKpy:Double?,
    val avgKPYUiModel:AvgKPYUiModel,
    val designsList:List<Int>
):Parcelable

@Parcelize
data class AvgKPYUiModel(
    val kyat:Double,
    val pae:Double,
    val ywae:Double
):Parcelable

fun JewelleryCategory.asUiModel():JewelleryCategoryUiModel{
    return JewelleryCategoryUiModel(
        id = id,
        name=name,
        imageUrlList = fileList.filter { it.type == "image" }.map { it.url },
        isChecked = false,
        isFrequentlyUse = isFrequentlyUse != "0",
        withGem = withGem != "0",
        orderToGs = orderToGs != "0",
        specification = specification,
        avgWeightPerUnitGm =avgWeightPerUnitGm,
        avgWastagePerUnitKpy =avgWastagePerUnitKpy,
        video = fileList.find { it.type == "video" }?.url,
        avgKPYUiModel = avgKPY.asUiModel(),
        designsList = designs
    )
}

fun AverageKPYDomain.asUiModel():AvgKPYUiModel{
    return AvgKPYUiModel(
        kyat= kyat,
        pae = pae,
        ywae = ywae
    )
}

