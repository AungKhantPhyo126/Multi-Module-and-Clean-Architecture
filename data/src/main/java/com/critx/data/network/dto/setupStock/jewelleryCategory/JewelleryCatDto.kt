package com.critx.data.network.dto.setupStock.jewelleryCategory

import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.jewelleryCategory.AverageKPYDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.CategoryFile
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality


data class JewelleryCatDto(
    val data:List<JewelleryCategoryData>
)

data class JewelleryCatCreatedData(
    val data:JewelleryCategoryData
)

data class JewelleryCategoryData(
    val id:String,
    val name:String,
    val is_frequently_used:String,
    val specification : String,
    val avg_weight_per_unit_gm:Double,
    val avg_wastage_per_unit_kpy:Double,
    val avg_KPY : AverageKPYData,
    val jewellery_type: JewelleryType,
    val jewellery_quality: JewelleryQuality,
    val group: JewelleryGroup,
    val files:List<CategoryFileData>,
    val designs:List<DesignData>
)
data class AverageKPYData(
    val kyat:Double,
    val pae :Double,
    val ywae:Double
)

data class CategoryFileData(
    val id:String,
    val type:String,
    val url:String
)

fun JewelleryCategoryData.asDomain():JewelleryCategory{
    return JewelleryCategory(
        id = id,
        name = name ,
        isFrequentlyUse = is_frequently_used,
        specification = specification,
        avgWeightPerUnitGm = avg_weight_per_unit_gm,
        avgWastagePerUnitKpy = avg_wastage_per_unit_kpy,
        jewelleryType = jewellery_type,
        jewelleryQuality = jewellery_quality,
        jewelleryGroup = group,
        fileList = files.map { it.asDomain() },
        avgKPY = avg_KPY.asDomain(),
        designs = designs.map { it.id.toInt() }
    )
}

fun AverageKPYData.asDomain():AverageKPYDomain{
    return AverageKPYDomain(
        kyat= kyat,
        pae = pae,
        ywae = ywae
    )
}

fun CategoryFileData.asDomain():CategoryFile{
    return CategoryFile(
        id = id,
        type = type,
        url =url
    )
}
