package com.critx.shwemiAdmin.screens.collectstock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectStockVIewModel @Inject constructor() :ViewModel(){
    private var _scannedStockcodebatch=MutableLiveData<MutableList<CollectStockBatchUIModel>>()
    val scannedStockcodebatch : LiveData<MutableList<CollectStockBatchUIModel>>
    get() = _scannedStockcodebatch
    private var dummyId= 0
    private val stockCodeList = mutableListOf<CollectStockBatchUIModel>()

    fun addStockCode(code:String){
        stockCodeList.add(CollectStockBatchUIModel(dummyId++.toString(),code))
        _scannedStockcodebatch.value=stockCodeList
    }
}