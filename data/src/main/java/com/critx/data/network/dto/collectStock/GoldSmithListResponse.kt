package com.critx.data.network.dto.collectStock

import com.critx.domain.model.collectStock.GoldSmithListDomain

data class GoldSmithListResponse(
     val data:List<GoldSmithListDto>
)
data class GoldSmithListDto(
    val id:String?,
    val name:String?
)

fun GoldSmithListDto.asDomain():GoldSmithListDomain{
    return GoldSmithListDomain(
        id = id.orEmpty(),
        name= name.orEmpty()
    )
}
