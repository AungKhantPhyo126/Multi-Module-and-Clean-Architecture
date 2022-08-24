package com.critx.shwemiAdmin.uistate

data class CreateProductUiState(
    var loading:Boolean = false,
    var success:String? = null,
    var error:String? = null,


    var calculateKPYLoading:Boolean =false,
    var calculateKPYSuccessLoading:Double? =null,
    var calculateKPYErrorMessage:String? =null
)