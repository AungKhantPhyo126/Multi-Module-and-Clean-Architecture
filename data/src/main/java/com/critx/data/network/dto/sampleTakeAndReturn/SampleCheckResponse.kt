package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleCheckResponse(
    val data:SampleCheckDto
)
data class SampleCheckDto(
    val id:String?,
    val product_code:String? = null,
    val thumbnail:String?,
    val specification:String?,
    val name:String?,
    val weight_gm:String?,
    val box_code:String?
)

fun SampleCheckDto.asDomain():SampleCheckDomain{
    return SampleCheckDomain(
       id,product_code,thumbnail,specification,name, weight_gm.orEmpty(), box_code = box_code.orEmpty()
    )
}