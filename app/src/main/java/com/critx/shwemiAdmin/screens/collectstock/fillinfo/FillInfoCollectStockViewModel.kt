package com.critx.shwemiAdmin.screens.collectstock.fillinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.collectStock.JewellerySizeDomain
import com.critx.domain.useCase.collectStock.CollectBatchUseCase
import com.critx.domain.useCase.collectStock.GetGoldSmithListUseCase
import com.critx.domain.useCase.collectStock.GetJewellerySizeUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.notifyObserverWithResource
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.collectStock.GoldSmithUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.JewellerySizeUIModel
import com.critx.shwemiAdmin.uiModel.collectStock.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class FillInfoCollectStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewellerySizeUseCase: GetJewellerySizeUseCase,
    private val collectBatchUseCase: CollectBatchUseCase,
    private val getGoldSmithListUseCase: GetGoldSmithListUseCase
):ViewModel() {

    private var _jewellerySizeLiveData= MutableLiveData<Resource<MutableList<JewellerySizeUIModel>>>()
    val jewellerySizeLiveData : LiveData<Resource<MutableList<JewellerySizeUIModel>>>
        get() = _jewellerySizeLiveData

    private var _goldSmithListLiveData= MutableLiveData<Resource<MutableList<GoldSmithUiModel>>>()
    val goldSmithListLiveData : LiveData<Resource<MutableList<GoldSmithUiModel>>>
        get() = _goldSmithListLiveData

    var selectedGoldSmith:String? = null

    private var _collectBatchLiveData = MutableLiveData<Resource<String>>()
    val collectBatchLiveData:LiveData<Resource<String>>
        get() = _collectBatchLiveData

    var selectedSize:String? = null

    fun selectSize(id:String){
//         var selectedSize = _jewellerySizeLiveData.value!!.data!!.find {
//            it.id == id
//        }
//        selectedSize!!.isChecked = selectedSize.isChecked.not()
//
//        var unselectedSize = _jewellerySizeLiveData.value!!.data!!.filter {
//            it.id != id
//        }
//
//        unselectedSize.forEach {
//            it.isChecked = false
//        }
        _jewellerySizeLiveData.value!!.data!!.filter {
            it.id != id
        }.forEach {
            it.isChecked = false
        }

        _jewellerySizeLiveData.value!!.data!!.find {
            it.id == id
        }?.isChecked = _jewellerySizeLiveData.value!!.data!!.find {
            it.id == id
        }?.isChecked!!.not()

        _jewellerySizeLiveData.value = _jewellerySizeLiveData.value

    }

    fun getJewellerySize(type:String){
        viewModelScope.launch {
           getJewellerySizeUseCase(localDatabase.getToken().orEmpty(),
            type).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _jewellerySizeLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewellerySizeLiveData.value = Resource.Success(it.data?.map { it.asUiModel() } as MutableList<JewellerySizeUIModel>)
                    }
                    is Resource.Error->{
                        _jewellerySizeLiveData.value = Resource.Error(it.message)
                    }
                }
            }

        }
    }

    fun collectBatch(
        kyat: RequestBody?,
        pae: RequestBody?,
        ywae: RequestBody?,
        goldSmithId: RequestBody?,
        bonus: RequestBody?,
        jewellerySizeId: RequestBody?,
        productIds: List<RequestBody>
    ){
        val method = "PUT"
        val methodRequestBody =method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            collectBatchUseCase(
                localDatabase.getToken().orEmpty(),
                methodRequestBody,
                kyat, pae, ywae, goldSmithId, bonus, jewellerySizeId, productIds
            ).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _collectBatchLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _collectBatchLiveData.value = Resource.Success(it.message)
                    }
                    is Resource.Error->{
                        _collectBatchLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getGoldSmithList(){
        viewModelScope.launch {
            getGoldSmithListUseCase(localDatabase.getToken().orEmpty(),"out").collectLatest {
                when(it){
                    is Resource.Loading->{
                        _goldSmithListLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _goldSmithListLiveData.value = Resource.Success(it.data!!.map { it.asUiModel() } as MutableList<GoldSmithUiModel>)
                    }
                    is Resource.Error->{
                        _goldSmithListLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}