package com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleItemUIModel(
    val sampleId:String,
    val productCode:String,
    val productId:String,
    val imageUrl:String,
    var specification:String?
)

fun SampleCheckDomain.asUIModel():SampleItemUIModel{
    return SampleItemUIModel(
        sampleId = sampleId,
        productCode = productCode,
        productId = productId,
        imageUrl = thumbnail,
        specification = specification
    )
}
