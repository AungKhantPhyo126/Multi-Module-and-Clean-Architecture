package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.dailygoldandprice.ProfileUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel

data class JewelleryTypeUiState(
    var loading:Boolean = false,
    var successLoading: List<JewelleryTypeUiModel>? = null,
    var errorMessage:String? =null
)
