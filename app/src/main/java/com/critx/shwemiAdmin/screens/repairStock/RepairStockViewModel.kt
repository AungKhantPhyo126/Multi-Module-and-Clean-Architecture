package com.critx.shwemiAdmin.screens.repairStock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.repairStock.JobDomain
import com.critx.domain.model.repairStock.JobDoneDomain
import com.critx.domain.useCase.collectStock.GetGoldSmithListUseCase
import com.critx.domain.useCase.repairStock.ChargeRepairStockUseCase
import com.critx.domain.useCase.repairStock.DeleteRepairStockUseCase
import com.critx.domain.useCase.repairStock.GetJobDoneDataUseCase
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
class RepairStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJobDoneDataUseCase: GetJobDoneDataUseCase,
    private val getGoldSmithListUseCase: GetGoldSmithListUseCase,
    private val chargeRepairStockUseCase: ChargeRepairStockUseCase,
    private val deleteRepairStockUseCase: DeleteRepairStockUseCase
) :ViewModel() {

    private var _jobDoneLiveData= MutableLiveData<Resource<JobDoneDomain>>()
    val jobDoneLiveData : LiveData<Resource<JobDoneDomain>>
        get() = _jobDoneLiveData

    private var _deleteRepairStockLiveData= MutableLiveData<Resource<String>>()
    val deleteRepairStockLiveData : LiveData<Resource<String>>
        get() = _deleteRepairStockLiveData
    fun resetJobDoneLiveData(){
        _jobDoneLiveData.value = null
    }
    fun removeJobDone(item:String){
        viewModelScope.launch {
            deleteRepairStockUseCase(item).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _deleteRepairStockLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _deleteRepairStockLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error->{
                        _deleteRepairStockLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    private var _chargeRepairStockLivedata= MutableLiveData<Resource<String>>()
    val chargeRepairStockLivedata : LiveData<Resource<String>>
        get() = _chargeRepairStockLivedata

    fun resetChargeRepairStockLiveData(){
        _chargeRepairStockLivedata.value = null
    }

    private var _goldSmithListLiveData= MutableLiveData<Resource<MutableList<GoldSmithUiModel>>>()
    val goldSmithListLiveData : LiveData<Resource<MutableList<GoldSmithUiModel>>>
        get() = _goldSmithListLiveData


    fun getJobDone(goldSmith:String){
        viewModelScope.launch {
            getJobDoneDataUseCase(localDatabase.getToken().orEmpty(),goldSmith).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _jobDoneLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _jobDoneLiveData.value = Resource.Success(it.data)
                    }
                    is Resource.Error->{
                        _jobDoneLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun chargeRepairStock(amount:RequestBody){
        viewModelScope.launch {
            val repairJobList = mutableListOf<RequestBody>()
                _jobDoneLiveData.value!!.data!!.data.map { it.id }.forEach {
                repairJobList.add(
                    it.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            }
            chargeRepairStockUseCase(
                localDatabase.getToken().orEmpty(),
                amount,
               repairJobList
            ).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _chargeRepairStockLivedata.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _chargeRepairStockLivedata.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error->{
                        _chargeRepairStockLivedata.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getGoldSmithList(){
        viewModelScope.launch {
            getGoldSmithListUseCase(localDatabase.getToken().orEmpty(),"in").collectLatest {
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