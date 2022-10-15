package com.critx.shwemiAdmin.uiModel.checkUpTransfer

import com.critx.domain.model.box.BoxScanDomain

data class BoxScanUIModel(
    val id:String,
    val code:String,
    val qty:String,
    val jewelleryType:String
)

fun BoxScanDomain.asUiModel():BoxScanUIModel{
    return BoxScanUIModel(
        id,code,qty, jewelleryType = jewellery_type
    )
}
