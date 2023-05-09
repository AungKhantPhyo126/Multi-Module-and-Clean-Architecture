package com.critx.data.network.dto.flashSales

import com.critx.domain.model.CustomerIdDomain

data class CustomerIdResponse(
    val data:CustomerIdDto
)
data class CustomerIdDto(
    val id:String,
    val code:String
)

fun CustomerIdDto.asDomain():CustomerIdDomain{
    return CustomerIdDomain(
        id, code
    )
}
