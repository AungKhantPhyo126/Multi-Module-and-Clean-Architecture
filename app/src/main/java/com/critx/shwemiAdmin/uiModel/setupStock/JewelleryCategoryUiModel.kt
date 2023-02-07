package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryCategory.AverageKPYDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.CategoryFile
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JewelleryCategoryUiModel(
    val id:String,
    val name:String,
    val imageUrlList:List<CategoryFileUiModel>,
    val video:String?,
    var isChecked:Boolean = false,
    var isFrequentlyUse:Boolean,
    var withGem:Boolean,
    var orderToGs:Boolean,
    val specification : String?,
    val avgWeightPerUnitGm:Double?,
    val avgWastagePerUnitYwae:Double?,
    val designsList:List<DesignUiModel>
):Parcelable

@Parcelize
data class AvgKPYUiModel(
    val kyat:Double,
    val pae:Double,
    val ywae:Double
):Parcelable

@Parcelize
data class CategoryFileUiModel(
    val id:String,
    val type:String,
    val url:String
):Parcelable


fun JewelleryCategory.asUiModel():JewelleryCategoryUiModel{
    return JewelleryCategoryUiModel(
        id = id,
        name=name,
        imageUrlList =fileList.filter { it.type == "image" }.map { it.asUiModel() },
        isChecked = false,
        isFrequentlyUse = isFrequentlyUse != "0",
        withGem = withGem != "0",
        orderToGs = orderToGs != "0",
        specification = specification,
        avgWeightPerUnitGm =avgWeightPerUnitGm,
        avgWastagePerUnitYwae =avgWastagePerUnitYwae,
        video = fileList.find { it.type == "video" }?.url,
        designsList = designs.map { it.asUiModel() }
    )
}

fun AverageKPYDomain.asUiModel():AvgKPYUiModel{
    return AvgKPYUiModel(
        kyat= kyat,
        pae = pae,
        ywae = ywae
    )
}

fun CategoryFile.asUiModel():CategoryFileUiModel{
    return CategoryFileUiModel(id, type, url)
}

