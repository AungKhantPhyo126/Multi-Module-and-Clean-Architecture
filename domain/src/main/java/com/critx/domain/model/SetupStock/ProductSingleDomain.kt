package com.critx.domain.model.SetupStock

import com.critx.domain.model.sampleTakeAndReturn.FileShweMiDomain

data class ProductSingleDomain(
    val category: String?,
    val code: String,
    val derived_gold_type: DerivedGoldTypeDomain,
    val derived_net_gold_weight_kpy: Double,
    val diamond_info: DiamondInfoDomain?,
    val files: List<FileShweMiDomain>,
    val gem_weight_ywae: Double,
    val gold_and_gem_weight_gm: Double,
    val goldsmith: String?,
    val group: String?,
    val id: String,
    val jewellery_quality: String,
    val jewellery_type: String,
    val name: String,
    val product_costs: ProductCostsDomain,
    val wastage_ywae: String?,
    val weight_including_label_gm: String?
)

data class DerivedGoldTypeDomain(
    val id: Int,
    val name: String,
    val per_unit: String,
    val price: Int
)
data class ProductCostsDomain(
    val bonus: String?,
    val gem_value: Int,
    val gold_net_value: Double,
    val maintenance_cost: Int,
    val pt_and_clip_cost: Int,
    val total_price_of_product: Double,
    val total_value_of_gem_and_gold: Double,
    val total_value_with_pt_maintenance: Double,
    val wastage_value: String?
)

data class DiamondInfoDomain(
    val diamond_info: String,
    val id: Int,
    val price_for_sale: Int,
    val price_from_goldsmith: Int,
    val product_id: String,
    val value_for_sale: Int,
    val value_from_goldsmith: Int
)
