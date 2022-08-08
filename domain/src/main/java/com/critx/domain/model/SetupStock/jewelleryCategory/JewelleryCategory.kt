package com.critx.domain.model.SetupStock.jewelleryCategory

import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality

data class JewelleryCategory(
    val id:String,
    val name:String,
    val isFrequentlyUse:String,
    val specification : String,
    val avgWeightPerUnitGm:Double,
    val avgWastagePerUnitKpy:Double,
    val jewelleryType: JewelleryType,
    val jewelleryQuality: JewelleryQuality,
    val jewelleryGroup:JewelleryGroup,
    val fileList:List<CategoryFile>
)
data class CategoryFile(
    val id:String,
    val type:String,
    val url:String
)
