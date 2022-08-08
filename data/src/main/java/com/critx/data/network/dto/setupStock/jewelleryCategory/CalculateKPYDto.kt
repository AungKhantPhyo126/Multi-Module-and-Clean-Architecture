package com.critx.data.network.dto.setupStock.jewelleryCategory

import com.critx.domain.model.SetupStock.jewelleryCategory.CalculateKPY

data class CalculateKPYDto(
    val kyat:Double,
    val gram:Double,
    val price:Double
)

fun CalculateKPYDto.asDomain():CalculateKPY{
    return CalculateKPY(
        kyat, gram, price
    )
}
