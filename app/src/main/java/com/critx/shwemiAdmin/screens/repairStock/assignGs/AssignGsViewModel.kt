package com.critx.shwemiAdmin.screens.repairStock.assignGs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.repairStock.RepairJobDomain
import com.critx.domain.useCase.SetUpStock.GetJewelleryTypeUseCase
import com.critx.domain.useCase.repairStock.AssignGoldSmithUseCase
import com.critx.domain.useCase.repairStock.GetRepairJobUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.collectStock.GoldSmithUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AssignGsViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewelleryTypeUseCase: GetJewelleryTypeUseCase,
    private val getRepairJobUseCase: GetRepairJobUseCase,
    private val assignGoldSmithUseCase: AssignGoldSmithUseCase
) :ViewModel(){

    private var _jewelleryTypeLiveData= MutableLiveData<Resource<List<JewelleryTypeUiModel>>>()
    val jewelleryTypeLiveData : LiveData<Resource<List<JewelleryTypeUiModel>>>
        get() = _jewelleryTypeLiveData

    private var _repairJobsLiveData= MutableLiveData<Resource<List<RepairJobDomain>>>()
    val repairJobsLiveData : LiveData<Resource<List<RepairJobDomain>>>
        get() = _repairJobsLiveData

    private var _assignGoldSmithLiveData= MutableLiveData<Resource<String>>()
    val assignGoldSmithLiveData : LiveData<Resource<String>>
        get() = _assignGoldSmithLiveData

    fun resetAssignGoldSmithLiveData(){
        _assignGoldSmithLiveData.value = null
    }

    var selectedJewelleryType = MutableLiveData<String>()
    var selectedRepairJob = MutableLiveData<String>()

    fun setJewelleryType(type:String){
        selectedJewelleryType.value = type
    }

    fun setRepairJob(job:String){
        selectedRepairJob.value = job
    }

    fun assignGoldSmith(goldSmithId:RequestBody,itemType:RequestBody,repairJob:RequestBody,weight:RequestBody,quantity:RequestBody){
        viewModelScope.launch {
            assignGoldSmithUseCase(localDatabase.getToken().orEmpty(),goldSmithId,
            itemType,repairJob,quantity,weight).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _assignGoldSmithLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _assignGoldSmithLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error->{
                        _assignGoldSmithLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getJewelleryType(){
        viewModelScope.launch {
            getJewelleryTypeUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _jewelleryTypeLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewelleryTypeLiveData.value = Resource.Success(it.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error->{
                        _jewelleryTypeLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getRepairJobs(jewelleryTypeId:String){
        viewModelScope.launch {
            getRepairJobUseCase(localDatabase.getToken().orEmpty(),jewelleryTypeId).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _repairJobsLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _repairJobsLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error->{
                        _repairJobsLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

}