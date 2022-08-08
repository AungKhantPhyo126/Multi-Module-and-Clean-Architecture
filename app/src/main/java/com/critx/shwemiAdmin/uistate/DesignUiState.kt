package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.setupStock.DesignUiModel

data class DesignUiState(
    var loading:Boolean = false,
    var success:List<DesignUiModel>? = null,
    var error:String? = null
)