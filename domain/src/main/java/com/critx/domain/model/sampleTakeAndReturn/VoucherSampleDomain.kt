package com.critx.domain.model.sampleTakeAndReturn
data class VoucherSampleDomain(
    val id:String,
    val sale_id:String,
    val product_id:String,
    val quantity:String,
    val specification:String,
    val name:String,
    val weight_gm:String,
    val type:String,
    val file:FileShweMiDomain
)

data class FileShweMiDomain(
    val id: String,
    val type: String,
    val url:String
)