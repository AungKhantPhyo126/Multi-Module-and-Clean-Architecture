package com.critx.data.network.dto.box

data class ProductScanResponse(
    val data:List<ProductScanDto>
)

data class ProductScanDto(
    val id:String?,
    val jewellery_type_id:String?
)
