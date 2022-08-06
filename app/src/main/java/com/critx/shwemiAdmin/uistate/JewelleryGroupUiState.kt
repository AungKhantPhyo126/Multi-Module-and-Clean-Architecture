package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel

data class JewelleryGroupUiState(
    var loading:Boolean = false,
    var successLoading: List<ChooseGroupUIModel>? = null,
    var errorMessage:String? =null,
    var createGroupLoading:Boolean = false,
    var createSuccessLoading:String? = null,
    var createErrorMessage:String? = null
)