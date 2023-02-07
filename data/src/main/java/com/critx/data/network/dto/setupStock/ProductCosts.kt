package com.critx.data.network.dto.setupStock

import com.critx.domain.model.SetupStock.ProductCostsDomain

data class ProductCosts(
    val bonus: String?,
    val gem_value: Int?,
    val gold_net_value: Double,
    val maintenance_cost: Int?,
    val pt_and_clip_cost: Int?,
    val total_price_of_product: Double?,
    val total_value_of_gem_and_gold: Double?,
    val total_value_with_pt_maintenance: Double?,
    val wastage_value: String?
)

fun ProductCosts.asDomain(): ProductCostsDomain {
    return ProductCostsDomain(
        bonus,
        gem_value ?: 0,
        gold_net_value,
        maintenance_cost ?: 0,
        pt_and_clip_cost ?: 0,
        total_price_of_product ?: 0.0,
        total_value_of_gem_and_gold ?: 0.0,
        total_value_with_pt_maintenance?:0.0,
        wastage_value
    )
}