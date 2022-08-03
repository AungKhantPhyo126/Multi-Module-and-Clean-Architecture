package com.critx.data.network.dto.setupStock.jewelleryType

import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType

data class JewelleryTypeData(
    val designs: Any?,
    val id: Int?,
    val image: Image?,
    val name: String?,
    val sizes: Any?
)

fun JewelleryTypeData.asDomain():JewelleryType{
    return JewelleryType(
        id = id.toString(),
        name = name.orEmpty()
    )
}