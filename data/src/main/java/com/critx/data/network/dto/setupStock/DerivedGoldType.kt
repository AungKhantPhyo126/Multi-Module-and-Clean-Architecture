package com.critx.data.network.dto.setupStock

import com.critx.domain.model.SetupStock.DerivedGoldTypeDomain

data class DerivedGoldType(
    val id: Int,
    val name: String,
    val per_unit: String,
    val price: Int
)

fun DerivedGoldType.asDomain():DerivedGoldTypeDomain{
    return DerivedGoldTypeDomain(id, name, per_unit, price)
}