package com.critx.shwemiAdmin.uistate

data class LoginUiState(
    var loading:Boolean = false,
    var successMessage:String? = null,
    var errorMessage:String? =null
)