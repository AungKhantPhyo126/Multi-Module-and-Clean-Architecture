package com.critx.data.network.dto.setupStock

import com.critx.data.network.dto.collectStock.JewellerySizeDto
import com.critx.data.network.dto.sampleTakeAndReturn.FileShweMiDto
import com.critx.data.network.dto.sampleTakeAndReturn.asDomain
import com.critx.domain.model.SetupStock.DiamondInfoDomain
import com.critx.domain.model.SetupStock.ProductSingleDomain
import com.squareup.moshi.Json

data class ProductSingleDto(
    val id: String,
    val code: String,
    val name: String,

    @field:Json(name = "jewellery_type")
    val jewellery_type: JewelleryType,
    val jewellery_quality: JewelleryQuality,
    val goldsmith: String?,
    val group: Group?,
    val category: Category?,

    @field:Json(name = "gold_and_gem_weight_gm")
    val gold_and_gem_weight_gm: Double,
    val gem_weight_ywae: Double,
    val avg_wastage_ywae: String?,
    val avg_weight_per_ywae: String?,
    val weight_including_label_gm: String?,
    val derived_net_gold_weight_kpy: Double,
    val derived_gold_type: DerivedGoldType,
    val files: List<FileShweMiDto>,
    val product_costs: ProductCosts,
    val diamond_info: DiamondInfoDto?
)

fun ProductSingleDto.asDomain():ProductSingleDomain{
    return ProductSingleDomain(
        category = category?.id,
        code,
        derived_gold_type = derived_gold_type.asDomain(),
        derived_net_gold_weight_kpy,diamond_info?.asDomain(), files = files.map { it.asDomain() },
        gem_weight_ywae,gold_and_gem_weight_gm,goldsmith.orEmpty(),
        group = group?.id,
        id,
        jewellery_quality = jewellery_quality.id.toString(),
        jewellery_type = jewellery_type.id.toString(),
        name, product_costs = product_costs.asDomain(),
        avg_wastage_ywae, weight_including_label_gm
    )
}

fun DiamondInfoDto.asDomain():DiamondInfoDomain{
    return DiamondInfoDomain(
        diamond_info = diamond_info.orEmpty(),
        id = id?:0,
        price_for_sale = price_for_sale?:0,
        price_from_goldsmith=price_from_goldsmith?:0,
        product_id=product_id.orEmpty(),
        value_for_sale = value_for_sale?:0,
        value_from_goldsmith = value_from_goldsmith?:0
    )
}
