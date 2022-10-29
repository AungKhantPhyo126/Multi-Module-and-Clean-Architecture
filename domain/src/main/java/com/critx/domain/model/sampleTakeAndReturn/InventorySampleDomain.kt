package com.critx.domain.model.sampleTakeAndReturn
data class OutsideSampleDomain(
    val file: FileShweMiDomain,
    val id: Int,
    val name:String,
    val weightGm:String,
    val specification: String,
    val type: Int,
    var isChecked:Boolean = false
)
data class ProductDomain(
    val code: String,
    val id: String
)
