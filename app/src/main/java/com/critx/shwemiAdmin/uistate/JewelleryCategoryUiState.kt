package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel

data class JewelleryCategoryUiState(
    var loading:Boolean = false,
    var successLoading: List<JewelleryCategoryUiModel>? = null,
    var errorMessage:String? =null,
    var createLoading:Boolean = false,
    var createSuccessLoading:JewelleryCategoryUiModel? = null,
    var createErrorMessage:String? = null,

    var editLoading:Boolean = false,
    var editSuccessLoading:String? = null,
    var editErrorMessage:String? = null,

    var getRelatedCatsLoading:Boolean = false,
    var getRelatedCatsSuccessLoading:List<JewelleryCategoryUiModel>? = null,
    var getRelatedCatsErrorMessage:String? = null,

    var calculateKPYLoading:Boolean =false,
    var calculateKPYSuccessLoading:Double? =null,
    var calculateKPYErrorMessage:String? =null,


    var deleteLoading:Boolean = false,
    var deleteSuccessLoading:String? = null,
    var deleteErrorMessage:String? = null
)
