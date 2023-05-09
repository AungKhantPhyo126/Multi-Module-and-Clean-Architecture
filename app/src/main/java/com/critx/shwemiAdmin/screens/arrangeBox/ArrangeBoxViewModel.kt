package com.critx.shwemiAdmin.screens.arrangeBox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.voucher.ScanVoucherToConfirmDomain
import com.critx.domain.useCase.box.ArrangeBoxesUseCase
import com.critx.domain.useCase.box.GetBoxDataUseCase
import com.critx.shwemiAdmin.SingleLiveEvent
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.BoxScanUIModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.asUiModel
import com.critx.shwemiAdmin.uiModel.checkupBoxWeight.BoxWeightUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArrangeBoxViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getBoxDataUseCase: GetBoxDataUseCase,
    private val arrangeBoxesUseCase: ArrangeBoxesUseCase
):ViewModel(){
    private var _arrangeBoxesLiveData = SingleLiveEvent<Resource<String>>()
    val arrangeBoxesLiveData: SingleLiveEvent<Resource<String>>
        get() = _arrangeBoxesLiveData
    private var _getBoxDataLive = MutableLiveData<Resource<BoxScanUIModel>>()
    val getBoxDataLive: LiveData<Resource<BoxScanUIModel>>
        get() = _getBoxDataLive

    private var _scannedBoxWeightLive = MutableLiveData<MutableList<BoxScanUIModel>>()
    val scannedBoxWeightLive: LiveData<MutableList<BoxScanUIModel>>
        get() = _scannedBoxWeightLive
    var boxList = mutableListOf<BoxScanUIModel>()

    fun addbox(item: BoxScanUIModel) {
        boxList.add(item)
        _scannedBoxWeightLive.value = boxList
    }
    fun removeBox(item: BoxScanUIModel) {
        boxList.remove(item)
        _scannedBoxWeightLive.value = boxList
    }

    fun resetBoxList(){
        boxList.removeAll(boxList)
        _scannedBoxWeightLive.value = boxList

    }

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

    fun arrangeBoxes(){
        viewModelScope.launch {
            arrangeBoxesUseCase(localDatabase.getToken().orEmpty(),
            boxList.map { it.id }).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _arrangeBoxesLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _arrangeBoxesLiveData.value = Resource.Success(it.data?.message.orEmpty())

                    }
                    is Resource.Error -> {
                        _arrangeBoxesLiveData.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }
}