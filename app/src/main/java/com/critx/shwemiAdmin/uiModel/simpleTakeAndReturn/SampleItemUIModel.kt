package com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain

data class SampleItemUIModel(
    val id:String?,
    val productCode:String? = null,
    val thumbnail:String?,
    var specification:String?,
    val name:String?,
    val weight_gm:String?,
    val box_code:String?,
    var isChecked:Boolean = false
)

fun SampleCheckDomain.asUIModel():SampleItemUIModel{
    return SampleItemUIModel(
        id, productCode, thumbnail, specification, name,weight_gm, box_code
    )
}
