package com.critx.data.network.dto.setupStock.jewelleryGroup

import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.asDomain
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeData
import com.critx.data.network.dto.setupStock.jewelleryType.asDomain
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup

data class Data(
    val id: Int?,
    val image: Image?,
    val is_frequently_used: Int?,
    val jewellery_quality: JewelleryQualityData,
    val jewellery_type: JewelleryTypeData,
    val name: String?
)

fun Data.asDomain():JewelleryGroup{
    return JewelleryGroup(
        id = id.toString(),
        image = image?.url.orEmpty(),
        isFrequentlyUse = is_frequently_used.toString() ,
        jewelleryQuality = jewellery_quality.asDomain(),
        jewelleryType = jewellery_type.asDomain(),
        name = name.orEmpty()
    )
}