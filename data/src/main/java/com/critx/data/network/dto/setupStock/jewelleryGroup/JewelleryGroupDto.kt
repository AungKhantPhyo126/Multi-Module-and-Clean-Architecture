package com.critx.data.network.dto.setupStock.jewelleryGroup

import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroupDomain

data class JewelleryGroupDto(
    val data: List<Data>,
    val links: Links,
    val meta: Meta
)

fun JewelleryGroupDto.asDomain():JewelleryGroupDomain{
    return JewelleryGroupDomain(
        data = data.map { it.asDomain() },
        links = links.asDomain(),
        meta = meta.asDomain()
    )
}