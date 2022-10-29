package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.model.sampleTakeAndReturn.OutsideSampleDomain
import com.critx.domain.useCase.sampleTakeAndReturn.AddToHandedListUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.GetOutsideSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.ReturnSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.SaveOutsideSampleUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class OutSideViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val saveOutsideSampleUseCase: SaveOutsideSampleUseCase,
    private val addToHandedListUseCase: AddToHandedListUseCase,
    private val getOutsideSampleUseCase: GetOutsideSampleUseCase,
    private val returnSampleUseCase: ReturnSampleUseCase


    ):ViewModel() {
    private val _getOutsideSampleLiveData = MutableLiveData<Resource<List<OutsideSampleDomain>>>()
    val getOutsideSampleLiveData: LiveData<Resource<List<OutsideSampleDomain>>>
        get() = _getOutsideSampleLiveData

    private var _saveOutsideSample = MutableLiveData<Resource<SimpleData>>()
    val saveOutsideSample: LiveData<Resource<SimpleData>>
        get() = _saveOutsideSample

    private var _returnSampleLiveData = MutableLiveData<Resource<String>>()
    val returnSampleLiveData: LiveData<Resource<String>>
        get() = _returnSampleLiveData

    fun resetReturnSampleLiveData(){
        _returnSampleLiveData.value = null
    }

    fun resetSaveOutSideSample(){
        _saveOutsideSample.value = null
    }

    var selectedImgUri= MutableLiveData<SelectedImage?>(null)
    fun setSelectedImgUri(selectedImage: SelectedImage?){
        selectedImgUri.value = selectedImage
    }

    fun resetSelectedImg(){
        selectedImgUri.value = null
    }

    private val _addToHandedListLiveData = MutableLiveData<Resource<String>>()
    val addToHandedListLiveData: LiveData<Resource<String>>
        get() = _addToHandedListLiveData
    fun resetAddtoHandleListLiveData(){
        _addToHandedListLiveData.value = null
    }

    fun getSelectedOutsideSample():List<String>{
        return _getOutsideSampleLiveData.value!!.data!!.filter {
            it.isChecked
        }.map { it.id.toString() }
    }

    fun returnSample(sampleIdList:List<String>){
        viewModelScope.launch {
            returnSampleUseCase(
                localDatabase.getToken().orEmpty(),
                sampleIdList).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _returnSampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _returnSampleLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _returnSampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }


    fun addToHandedList(sampleList:List<String>) {
        viewModelScope.launch {
            addToHandedListUseCase(
                localDatabase.getToken().orEmpty(),
                sampleList).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _addToHandedListLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _addToHandedListLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _addToHandedListLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getOutSideSampleList() {
        viewModelScope.launch {
            getOutsideSampleUseCase(
                localDatabase.getToken().orEmpty()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getOutsideSampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getOutsideSampleLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _getOutsideSampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun selectSampleForReturn(id:String){
        val targetItem = _getOutsideSampleLiveData.value!!.data!!.find { it.id.toString() ==id }
        targetItem!!.isChecked = targetItem.isChecked.not()
        _getOutsideSampleLiveData.value = _getOutsideSampleLiveData.value
    }

    fun saveOusideSample(
        name:RequestBody?,
        weight:RequestBody?,
        specification:RequestBody?,
        image:MultipartBody.Part
    ){
        viewModelScope.launch {
            saveOutsideSampleUseCase(localDatabase.getToken().orEmpty(),
            name,weight, specification,image).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _saveOutsideSample.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _saveOutsideSample.value = Resource.Success(it.data!!)

                    }
                    is Resource.Error -> {
                        _saveOutsideSample.value = Resource.Error(it.message)
                    }
                }

            }
        }
    }

}