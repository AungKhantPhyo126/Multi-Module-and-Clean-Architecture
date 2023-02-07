package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.box.GetBoxDataUseCase
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.transferCheckUp.CheckUpUseCase
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.BoxScanUIModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.CheckUpUiModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.asUIModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckUpStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getBoxDataUseCase: GetBoxDataUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val checkUpUseCase: CheckUpUseCase
):ViewModel() {
    var targetBoxCode:String? = null
    private var _getBoxDataLive = MutableLiveData<Resource<BoxScanUIModel>>()
    val getBoxDataLive: LiveData<Resource<BoxScanUIModel>>
        get() = _getBoxDataLive

    private var _scanStockLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanStockLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanStockLive

    private var _stockListLive = MutableLiveData<MutableList<StockCodeForListUiModel>>()
    val stockListLive: LiveData<MutableList<StockCodeForListUiModel>>
        get() = _stockListLive

    private var _checkUpStockLive = MutableLiveData<Resource<CheckUpUiModel>>()
    val checkUpStockLive: LiveData<Resource<CheckUpUiModel>>
        get() = _checkUpStockLive
    fun resetCheckUpStockLive(){
        _checkUpStockLive.value= null
    }

    var stockCodeList = mutableListOf<StockCodeForListUiModel>()

    fun addStockCode(stockItem: StockCodeForListUiModel) {
        stockCodeList.add(stockItem)
        _stockListLive.value = stockCodeList
    }

    fun removeStockCode(item: StockCodeForListUiModel) {
        stockCodeList.remove(item)
        _stockListLive.value = stockCodeList
    }

    fun resetStockCodeList(){
        stockCodeList.removeAll(stockCodeList)
        _stockListLive.value = mutableListOf()
    }

    fun resetScanStockLive(){
        _scanStockLive.value= null
    }

    fun checkUp(boxCode: String,productIdList:List<String>){
        viewModelScope.launch {
            checkUpUseCase(localDatabase.getToken().orEmpty(),boxCode,productIdList).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _checkUpStockLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _checkUpStockLive.value = Resource.Success(it.data!!.asUIModel())

                    }
                    is Resource.Error -> {
                        _checkUpStockLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun scanStock(stockCode:String){
        viewModelScope.launch {
            scanProductCodeUseCase(localDatabase.getToken().orEmpty(),stockCode).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _scanStockLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _scanStockLive.value = Resource.Success(it.data)

                    }
                    is Resource.Error -> {
                        _scanStockLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun getBoxData(boxCode:String){
        viewModelScope.launch {
            getBoxDataUseCase(localDatabase.getToken().orEmpty(),boxCode).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getBoxDataLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        targetBoxCode = it.data!!.code
                        _getBoxDataLive.value = Resource.Success(it.data!!.asUiModel())

                    }
                    is Resource.Error -> {
                        _getBoxDataLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }
}