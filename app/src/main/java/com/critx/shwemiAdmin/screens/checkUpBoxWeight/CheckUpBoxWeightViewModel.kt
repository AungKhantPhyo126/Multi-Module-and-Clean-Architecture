package com.critx.shwemiAdmin.screens.checkUpBoxWeight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.box.GetBoxDataUseCase
import com.critx.domain.useCase.box.GetBoxWeightUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.BoxScanUIModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.asUiModel
import com.critx.shwemiAdmin.uiModel.checkupBoxWeight.BoxWeightUiModel
import com.critx.shwemiAdmin.uiModel.checkupBoxWeight.asUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckUpBoxWeightViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getBoxDataUseCase: GetBoxDataUseCase,
    private val getBoxWeightUseCase: GetBoxWeightUseCase
):ViewModel() {

    private var _scannedBoxWeightLive = MutableLiveData<MutableList<BoxWeightUiModel>>()
    val scannedBoxWeightLive: LiveData<MutableList<BoxWeightUiModel>>
        get() = _scannedBoxWeightLive
    var boxList = mutableListOf<BoxWeightUiModel>()

    fun addbox(stockItem: BoxWeightUiModel) {
        boxList.add(stockItem)
        _scannedBoxWeightLive.value = boxList
    }

    fun resetAll() {
        boxList.removeAll(boxList)
        _scannedBoxWeightLive.value = boxList
        _getBoxDataLive.value = null
        _checkUpBoxWeightLive.value = null
    }

    private var _getBoxDataLive = MutableLiveData<Resource<BoxScanUIModel>>()
    val getBoxDataLive: LiveData<Resource<BoxScanUIModel>>
        get() = _getBoxDataLive

    private var _checkUpBoxWeightLive = MutableLiveData<Resource<List<BoxWeightUiModel>>>()
    val checkUpBoxWeightLive: LiveData<Resource<List<BoxWeightUiModel>>>
        get() = _checkUpBoxWeightLive

    fun getBoxData(boxCode:String){
        viewModelScope.launch {
            getBoxDataUseCase(localDatabase.getToken().orEmpty(),boxCode).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getBoxDataLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getBoxDataLive.value = Resource.Success(it.data!!.asUiModel())

                    }
                    is Resource.Error -> {
                        _getBoxDataLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun checkBoxWeight(boxCodeList:List<String>){
        viewModelScope.launch {
            getBoxWeightUseCase(localDatabase.getToken().orEmpty(),boxCodeList).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _checkUpBoxWeightLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _checkUpBoxWeightLive.value = Resource.Success(it.data!!.map { it.asUiModel() })

                    }
                    is Resource.Error -> {
                        _checkUpBoxWeightLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }
}