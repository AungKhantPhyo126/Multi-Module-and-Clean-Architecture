package com.critx.data.network.dto.box

data class BoxScanResponse(
    val data:BoxScanDto
)

data class BoxScanDto(
    val id:String?,
    val code:String?,
    val qty:String?,
    val jewellery_type:String?
)
