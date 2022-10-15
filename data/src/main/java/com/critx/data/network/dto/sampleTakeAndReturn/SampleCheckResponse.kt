package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleCheckResponse(
    val data:List<SampleCheckDto>
)
data class SampleCheckDto(
    val productId:String?,
    val thumbnail:String?,
    val sampleId:String?,
    val specification:String?
)

fun SampleCheckDto.asDomain():SampleCheckDomain{
    return SampleCheckDomain(
        productId = productId.orEmpty(),
        thumbnail = thumbnail.orEmpty(),
        sampleId = sampleId.orEmpty(),
        specification = specification.orEmpty()
    )
}