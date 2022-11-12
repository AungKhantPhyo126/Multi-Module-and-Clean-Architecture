package com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer.matchCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.transferCheckUp.TransferUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.BoxScanUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchCodeViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val transferUseCase: TransferUseCase
) :ViewModel(){
    var rfidCodeList = mutableListOf<String>()

    var rfidScanPosition:Int? =null
    val rfidScanCacheLiveData = MutableLiveData<String>()

    private var _transferLiveData = MutableLiveData<Resource<String>>()
    val transferLiveData: LiveData<Resource<String>>
        get() = _transferLiveData

    fun resetTransferLiveData(){
        _transferLiveData.value = null
    }


    fun transferStock(boxCode:String,productIdList:List<String>,rfidCode:HashMap<String,String>){
        viewModelScope.launch {
            transferUseCase(localDatabase.getToken().orEmpty(),boxCode,
            productIdList,rfidCode).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _transferLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _transferLiveData.value = Resource.Success(it.data!!.message)

                    }
                    is Resource.Error->{
                        _transferLiveData.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }
}