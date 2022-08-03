package com.critx.data.network.dto.setupStock.jewelleryQuality

import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality

class JewelleryQualityDto:ArrayList<JewelleryQualityData>()
data class JewelleryQualityData(
    val id:Int?,
    val name:String?
)

fun JewelleryQualityData.asDomain():JewelleryQuality{
    return JewelleryQuality(
        id =id.toString(),
        name = name.orEmpty()
    )
}
