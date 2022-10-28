package com.critx.domain.model.sampleTakeAndReturn
data class InventorySampleDomain(
    val file: FileShweMiDomain,
    val id: Int,
    val product: ProductDomain,
    val specification: String,
    val type: Int
)
data class ProductDomain(
    val code: String,
    val id: String
)
