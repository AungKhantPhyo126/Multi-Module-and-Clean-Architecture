package com.critx.domain.model.SetupStock.jewelleryCategory

import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality

data class JewelleryCategory(
    val id:String,
    val name:String,
    val isFrequentlyUse:String,
    val withGem:String,
    val orderToGs:String,
    val specification : String,
    val avgWeightPerUnitGm:Double,
    val avgWastagePerUnitYwae:Double,
    val jewelleryType: JewelleryType,
    val jewelleryQuality: JewelleryQuality,
    val jewelleryGroup:JewelleryGroup,
    val fileList:List<CategoryFile>,
    val designs:List<DesignDomain>
)
data class AverageKPYDomain(
    val kyat:Double,
    val pae:Double,
    val ywae:Double
)
data class CategoryFile(
    val id:String,
    val type:String,
    val url:String
)
