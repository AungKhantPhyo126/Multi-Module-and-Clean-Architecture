package com.critx.shwemiAdmin.screens.giveGold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.giveGold.GoldBoxDomain
import com.critx.domain.model.repairStock.JobDoneDomain
import com.critx.domain.useCase.collectStock.GetGoldSmithListUseCase
import com.critx.domain.useCase.giveGold.GetGoldBoxIdUseCase
import com.critx.domain.useCase.giveGold.GiveGoldUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.collectStock.GoldSmithUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiveGoldViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val giveGoldUseCase: GiveGoldUseCase,
    private val getGoldSmithListUseCase: GetGoldSmithListUseCase,
    private val getGoldBoxIdUseCase: GetGoldBoxIdUseCase
) : ViewModel() {



    private var _giveGoldLiveData = MutableLiveData<Resource<String>>()
    val giveGoldLiveData: LiveData<Resource<String>>
        get() = _giveGoldLiveData
    fun resetGiveGoldLiveData(){
        _giveGoldLiveData.value = null
    }

    private var _getGoldBoxLiveData = MutableLiveData<Resource<List<GoldBoxDomain>>>()
    val getGoldBoxLiveData: LiveData<Resource<List<GoldBoxDomain>>>
        get() = _getGoldBoxLiveData


    private var _goldSmithListLiveData= MutableLiveData<Resource<MutableList<GoldSmithUiModel>>>()
    val goldSmithListLiveData : LiveData<Resource<MutableList<GoldSmithUiModel>>>
        get() = _goldSmithListLiveData

    fun getGoldSmithList(){
        viewModelScope.launch {
            getGoldSmithListUseCase(localDatabase.getToken().orEmpty()).collectLatest {
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

    fun getGoldBoxId(){
        viewModelScope.launch {
            getGoldBoxIdUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _getGoldBoxLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _getGoldBoxLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error->{
                        _getGoldBoxLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun giveGold(
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightK: String,
        weighP: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight:String,
        wastageK: String,
        wastageP: String,
        wastageY: String,
        dueDate: String,
        sampleList: List<String>?
    ) {
        viewModelScope.launch {
            giveGoldUseCase(
                localDatabase.getToken().orEmpty(),
                goldSmithId,
                orderItem,
                orderQty,
                weightK,
                weighP,
                weightY,
                goldBoxId,
                goldWeight,
                gemWeight,
                goldAndGemWeight,
                wastageK,
                wastageP,
                wastageY,
                dueDate,
                sampleList
            ).collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _giveGoldLiveData.value = Resource.Loading()
                }
                is Resource.Success -> {
                    _giveGoldLiveData.value = Resource.Success(it.data!!.message)
                }
                is Resource.Error -> {
                    _giveGoldLiveData.value = Resource.Error(it.message)
                }
            }
        }
        }
    }


}