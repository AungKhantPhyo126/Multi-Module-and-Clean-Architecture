package com.critx.data.network.dto.setupStock.jewelleryGroup

data class Links(
    val first: String?,
    val last: String?,

)

fun Links.asDomain():com.critx.domain.model.SetupStock.pagingRelated.Links{
    return com.critx.domain.model.SetupStock.pagingRelated.Links(
        first = first.orEmpty(),
        last = last.orEmpty(),
    )
}