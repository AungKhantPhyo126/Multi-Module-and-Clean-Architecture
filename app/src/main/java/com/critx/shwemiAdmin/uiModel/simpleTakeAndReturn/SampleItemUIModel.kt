package com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleItemUIModel(
    val id:String,
    val stockCode:String,
    val imageUrl:String,
    val specification:String?
)

fun SampleCheckDomain.asUIModel():SampleItemUIModel{
    return SampleItemUIModel(
        id = sampleId,
        stockCode = productId,
        imageUrl = thumbnail,
        specification = specification
    )
}
