package com.critx.data.network.dto.setupStock.jewelleryGroup

data class Links(
    val first: String,
    val last: String,
    val next: Any,
    val prev: Any
)

fun Links.asDomain():com.critx.domain.model.SetupStock.pagingRelated.Links{
    return com.critx.domain.model.SetupStock.pagingRelated.Links(
        first = first,
        last = last,
        next = next,
        prev = prev
    )
}