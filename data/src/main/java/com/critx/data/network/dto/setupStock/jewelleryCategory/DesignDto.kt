package com.critx.data.network.dto.setupStock.jewelleryCategory

import com.critx.domain.model.SetupStock.jewelleryCategory.DesignDomain

data class DesignDto(
    val data:List<DesignData>
)
data class DesignData(
    val id:String,
    val name:String
)

fun DesignData.asDomain():DesignDomain{
    return DesignDomain(
        id, name
    )
}
