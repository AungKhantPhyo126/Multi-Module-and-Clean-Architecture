package com.critx.data.network.dto.orderStock

import com.critx.data.network.dto.collectStock.GoldSmithListDto
import com.critx.data.network.dto.collectStock.asDomain
import com.critx.data.network.dto.sampleTakeAndReturn.FileShweMiDto
import com.critx.data.network.dto.sampleTakeAndReturn.asDomain
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkStockInfoDomain
import com.critx.domain.model.orderStock.BookMarkedStocksWithPaging

data class GsNewItemsResponse(
    val data: List<GsNewItemsDto>?,
    val meta: PagingMetaDto?
)
data class GsNewItemsDto(
    val id:String,
    val file:FileShweMiDto,
    val gold_weight_ywae:String,
    val jewellery_type_id:String,
    val name:String?,
    val sizes:List<BookMarkStockInfoDomain>,
    val is_orderable:Boolean,
    val goldsmith:GoldSmithListDto,
    val gold_quality:String?
)

fun GsNewItemsDto.asDomain(): BookMarkStockDomain{
    return BookMarkStockDomain(
        id ,
        image = file.asDomain(),
        avg_unit_weight_ywae =  gold_weight_ywae,
        jewellery_type_id = jewellery_type_id,
        custom_category_name = name.orEmpty(),
        sizes = sizes,
        is_orderable = is_orderable,
        goldSmith = goldsmith.asDomain(),
        gold_quality
    )
}

fun GsNewItemsResponse.asDomain(): BookMarkedStocksWithPaging {
    return BookMarkedStocksWithPaging(
        data = data?.map { it.asDomain() }.orEmpty(),
        meta = meta?.asDomain()
    )
}