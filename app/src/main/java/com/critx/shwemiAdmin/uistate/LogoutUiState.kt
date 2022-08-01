package com.critx.shwemiAdmin.uistate

data class LogoutUiState (
    var loading:Boolean = false,
    var successMessage:String? = null,
    var errorMessage:String? =null
)