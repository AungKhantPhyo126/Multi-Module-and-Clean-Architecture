package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.notifyObserverWithResource
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.VoucherForSampleUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoucherViewModel @Inject constructor(
    private val localDatabase: LocalDatabase
):ViewModel(){
    private val _voucherLiveData = MutableLiveData<Resource<MutableList<VoucherForSampleUIModel>>>()
    val voucherLiveData:LiveData<Resource<MutableList<VoucherForSampleUIModel>>>
        get() = _voucherLiveData

    fun addStockSample(item:VoucherForSampleUIModel){
        _voucherLiveData.value?.data?.add(item)
        _voucherLiveData.notifyObserverWithResource()
    }

    fun removeSample(item:VoucherForSampleUIModel){
        _voucherLiveData.value?.data?.remove(item)
        _voucherLiveData.notifyObserverWithResource()

    }

}
