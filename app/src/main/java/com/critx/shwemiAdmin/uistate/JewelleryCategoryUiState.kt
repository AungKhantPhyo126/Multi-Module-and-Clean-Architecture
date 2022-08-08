package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel

data class JewelleryCategoryUiState(
    var loading:Boolean = false,
    var successLoading: List<JewelleryCategoryUiModel>? = null,
    var errorMessage:String? =null,
    var createLoading:Boolean = false,
    var createSuccessLoading:String? = null,
    var createErrorMessage:String? = null,

    var calculateKPYLoading:Boolean =false,
    var calculateKPYSuccessLoading:Double? =null,
    var calculateKPYErrorMessage:String? =null
)
