package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
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
    private val saveOutsideSampleUseCase: SaveOutsideSampleUseCase
):ViewModel() {
    private var _saveOutsideSample = MutableLiveData<Resource<String>>()
    val saveOutsideSample: LiveData<Resource<String>>
        get() = _saveOutsideSample

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
                        _saveOutsideSample.value = Resource.Success(it.data?.message)

                    }
                    is Resource.Error -> {
                        _saveOutsideSample.value = Resource.Error(it.message)
                    }
                }

            }
        }
    }

}