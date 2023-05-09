package com.critx.domain.model.orderStock

import com.critx.domain.model.collectStock.GoldSmithListDomain
import com.critx.domain.model.sampleTakeAndReturn.FileShweMiDomain
import com.squareup.moshi.Json
data class BookMarkedStocksWithPaging(
    val data: List<BookMarkStockDomain>?,
    val meta: PagingMetaDomain?
)

data class BookMarkStockDomain(
    val id:String,
    val image:FileShweMiDomain,
    val avg_unit_weight_ywae:String,
    val jewellery_type_id:String,
    val custom_category_name:String,
    val sizes:List<BookMarkStockInfoDomain>,
    val is_orderable:Boolean,
    val goldSmith:GoldSmithListDomain?,
    val goldQuality:String?,
    val isFromCloud:Boolean
)

data class PagingMetaDomain(
    val currentPage: Int?,
    val lastPage: Int?,
    val from: Int?,
    val to: Int?,
    val total: Int?
)