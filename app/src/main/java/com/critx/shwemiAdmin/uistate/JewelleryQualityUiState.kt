package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel

data class JewelleryQualityUiState(
    var loading:Boolean = false,
    var successLoading: List<JewelleryQualityUiModel>? = null,
    var errorMessage:String? =null
)