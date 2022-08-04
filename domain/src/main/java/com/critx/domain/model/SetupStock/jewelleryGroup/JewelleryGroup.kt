package com.critx.domain.model.SetupStock.jewelleryGroup

import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality

data class JewelleryGroup(
    val id:String,
    val name:String,
    val isFrequentlyUse:String,
    val jewelleryType:JewelleryType,
    val jewelleryQuality:JewelleryQuality,
    val image:String
)
