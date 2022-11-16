package com.critx.data.network.dto.orderStock

import com.critx.data.network.dto.sampleTakeAndReturn.FileShweMiDto
import com.critx.data.network.dto.sampleTakeAndReturn.asDomain
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkedStocksWithPaging
import com.critx.domain.model.orderStock.PagingMetaDomain
import com.squareup.moshi.Json

data class BookMarkedStocksResponse(
    val data: List<BookMarkStockDto>?,
    val meta: PagingMetaDto?
)

data class BookMarkStockDto(
    val id:String,
    val image:FileShweMiDto,
    val avg_weight_per_unit_kyat:String,
    val avg_weight_per_unit_pae:String,
    val avg_weight_per_unit_ywae:String,
    val jewellery_type_id:String,
)

data class PagingMetaDto(
    @field:Json(name = "current_page")
    val currentPage: Int?,
    @field:Json(name = "last_page")
    val lastPage: Int?,
    val from: Int?,
    val to: Int?,
    val total: Int?
)

fun BookMarkStockDto.asDomain():BookMarkStockDomain{
    return BookMarkStockDomain(
        id,
        image.asDomain(),
        avg_weight_per_unit_kyat,
        avg_weight_per_unit_pae,
        avg_weight_per_unit_ywae,
        jewellery_type_id
    )
}

fun PagingMetaDto.asDomain():PagingMetaDomain{
    return PagingMetaDomain(
       currentPage, lastPage, from, to, total
    )
}

fun BookMarkedStocksResponse.asDomain():BookMarkedStocksWithPaging{
    return BookMarkedStocksWithPaging(
        data = data?.map { it.asDomain() }.orEmpty(),
        meta = meta?.asDomain()
    )
}
