package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel

data class JewelleryGroupUiState(
    var loading:Boolean = false,
    var successLoading: List<ChooseGroupUIModel>? = null,
    var errorMessage:String? =null,
    var createGroupLoading:Boolean = false,
    var createSuccessLoading:ChooseGroupUIModel? = null,
    var createErrorMessage:String? = null,

    var editGroupLoading:Boolean = false,
    var editSuccessLoading:String? = null,
    var editErrorMessage:String? = null
)