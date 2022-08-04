package com.critx.data.network.dto.setupStock.jewelleryGroup

data class Meta(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
)

fun Meta.asDomain():com.critx.domain.model.SetupStock.pagingRelated.Meta{
    return com.critx.domain.model.SetupStock.pagingRelated.Meta(
        current_page = current_page,
        from = from,
        last_page = last_page,
        path=path,
        per_page = per_page,
        to = to,
        total=total
    )
}