package com.critx.data.network.dto.orderStock

import com.critx.domain.model.orderStock.BookMarkStockInfoDomain

data class BookMarkStockInfoResponse(
    val data:List<BookMarkStockInfoDto>
)

data class BookMarkStockInfoDto(
    val id:String,
    val size:String,
    val stock:String
)

fun BookMarkStockInfoDto.asDomain():BookMarkStockInfoDomain{
    return BookMarkStockInfoDomain(
        id, size, stock
    )
}