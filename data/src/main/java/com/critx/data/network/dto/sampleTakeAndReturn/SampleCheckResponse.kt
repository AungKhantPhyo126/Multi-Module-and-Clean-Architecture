package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleCheckResponse(
    val data:SampleCheckDto
)
data class SampleCheckDto(
    val id:String?,
    val productCode:String? = null,
    val thumbnail:String?,
    val specification:String?,
    val weight_gm:String?,
    val box_code:String?
)

fun SampleCheckDto.asDomain():SampleCheckDomain{
    return SampleCheckDomain(
       id, productCode, thumbnail, specification, weight_gm, box_code
    )
}