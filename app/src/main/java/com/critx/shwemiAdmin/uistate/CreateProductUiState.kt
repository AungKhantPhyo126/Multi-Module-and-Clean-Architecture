package com.critx.shwemiAdmin.uistate

data class CreateProductUiState(
    var loading:Boolean = false,
    var success:String? = null,
    var error:String? = null,

    var getProductCodeLoading:Boolean =false,
    var getProductCodeSuccess:String? =null,
    var getProductCodeError:String? =null
)