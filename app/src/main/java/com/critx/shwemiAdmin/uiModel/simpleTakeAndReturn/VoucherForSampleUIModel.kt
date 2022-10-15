package com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn

data class VoucherForSampleUIModel(
    val id:String,
    val invoiceCode:String,
    val totalQty:String,
    val stockList:List<StockInVocuherUIModel>
)

data class StockInVocuherUIModel(
    val id:String,
    val stockName:String,
    val stockCount:String
)
