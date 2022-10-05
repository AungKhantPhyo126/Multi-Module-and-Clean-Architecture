package com.critx.domain.model.sampleTakeAndReturn

data class HandedListDomain(
    val id:String,
    val product_id:String,
    val specification:String,
    val name:String,
    val weight:String,
    val type:String,
    val file:FileShweMiDomain
)
