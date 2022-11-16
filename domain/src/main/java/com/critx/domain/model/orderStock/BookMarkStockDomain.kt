package com.critx.domain.model.orderStock

import com.critx.domain.model.sampleTakeAndReturn.FileShweMiDomain
import com.squareup.moshi.Json
data class BookMarkedStocksWithPaging(
    val data: List<BookMarkStockDomain>?,
    val meta: PagingMetaDomain?
)

data class BookMarkStockDomain(
    val id:String,
    val image:FileShweMiDomain,
    val avg_weight_per_unit_kyat:String,
    val avg_weight_per_unit_pae:String,
    val avg_weight_per_unit_ywae:String,
    val jewellery_type_id:String,
)

data class PagingMetaDomain(
    val currentPage: Int?,
    val lastPage: Int?,
    val from: Int?,
    val to: Int?,
    val total: Int?
)