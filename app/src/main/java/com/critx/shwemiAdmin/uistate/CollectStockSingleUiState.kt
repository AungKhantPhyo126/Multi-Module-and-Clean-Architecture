package com.critx.shwemiAdmin.uistate

data class CollectStockSingleUiState(
    var getProductIdLoading:Boolean = false,
    var getProductIdSuccess:String? = null,
    var getProductIdError:String? = null,
)