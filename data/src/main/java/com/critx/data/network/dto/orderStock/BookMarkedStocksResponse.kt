package com.critx.data.network.dto.orderStock

import com.critx.data.network.dto.collectStock.GoldSmithListDto
import com.critx.data.network.dto.collectStock.asDomain
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
    val avg_unit_weight_ywae:String,
    val jewellery_type_id:String,
    val name:String?,
    val sizes:List<BookMarkStockInfoDto>?,
    val is_orderable:Boolean?,

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
        avg_unit_weight_ywae,
        jewellery_type_id,
        name.orEmpty(),
        sizes?.map { it.asDomain() }.orEmpty(),
        is_orderable?:true,
        null,
        null,
        true
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
