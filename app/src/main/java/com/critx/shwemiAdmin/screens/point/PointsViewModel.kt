package com.critx.shwemiAdmin.screens.point

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.CustomerIdDomain
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.flashSales.GetUserPointsUseCase
import com.critx.domain.useCase.flashSales.ManualPointsAddorReduceUseCase
import com.critx.domain.useCase.flashSales.UserScanUseCase
import com.critx.shwemiAdmin.SingleLiveEvent
import com.critx.shwemiAdmin.uiModel.UserPointsUiModel
import com.critx.shwemiAdmin.uiModel.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PointsViewModel @Inject constructor(
    private val getUserPointsUseCase: GetUserPointsUseCase,
    private val localDatabase: LocalDatabase,
    private val manualPointsAddorReduceUseCase: ManualPointsAddorReduceUseCase,
    private val userScanUseCase: UserScanUseCase
) :ViewModel() {
    private var _userScanLiveData = SingleLiveEvent<Resource<CustomerIdDomain>>()
    val userScanLiveData: SingleLiveEvent<Resource<CustomerIdDomain>>
        get() = _userScanLiveData
    private var _getUserPointsLiveData = SingleLiveEvent<Resource<UserPointsUiModel>>()
    val getUserPointsLiveData: SingleLiveEvent<Resource<UserPointsUiModel>>
        get() = _getUserPointsLiveData


    private var _manualPointsAddorReduceLive = SingleLiveEvent<Resource<String>>()
    val manualPointsAddorReduceLive: SingleLiveEvent<Resource<String>>
        get() = _manualPointsAddorReduceLive

    fun getUserPoints(userId:String){
        viewModelScope.launch {
            getUserPointsUseCase(localDatabase.getToken().orEmpty(),userId).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _getUserPointsLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _getUserPointsLiveData.value = Resource.Success(it.data!!.asUiModel())
                    }
                    is Resource.Error->{
                        _getUserPointsLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
    fun manualPointsAddorReduce(
        user_id:RequestBody,
        point:RequestBody,
        reason:RequestBody,
        action:RequestBody,
    ){
        viewModelScope.launch {
            manualPointsAddorReduceUseCase(localDatabase.getToken().orEmpty(),
            user_id, point, reason, action).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _manualPointsAddorReduceLive.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _manualPointsAddorReduceLive.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error->{
                        _manualPointsAddorReduceLive.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun userScan(userCode:String){
        viewModelScope.launch {
            userScanUseCase(localDatabase.getToken().orEmpty(),userCode).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _userScanLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _userScanLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error->{
                        _userScanLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}