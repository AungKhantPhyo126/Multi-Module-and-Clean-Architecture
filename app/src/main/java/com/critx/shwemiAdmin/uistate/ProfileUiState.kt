package com.critx.shwemiAdmin.uistate

import com.critx.shwemiAdmin.uiModel.dailygoldandprice.ProfileUIModel

data class ProfileUiState(
    var loading:Boolean = false,
    var successLoading:ProfileUIModel? = null,
    var errorMessage:String? =null
)
