package com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleItemUIModel(
    val id:String,
    val productId:String,
    val imageUrl:String,
    var specification:String?
)

fun SampleCheckDomain.asUIModel():SampleItemUIModel{
    return SampleItemUIModel(
        id = sampleId,
        productId = productId,
        imageUrl = thumbnail,
        specification = specification
    )
}
