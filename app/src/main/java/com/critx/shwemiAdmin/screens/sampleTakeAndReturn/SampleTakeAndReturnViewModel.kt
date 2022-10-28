package com.critx.shwemiAdmin.screens.sampleTakeAndReturn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.sampleTakeAndReturn.HandedListDomain
import com.critx.domain.useCase.sampleTakeAndReturn.GetHandedListUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SAMPLE_TAKE_STATE= "sample"
const val GIVE_GOLD_STATE= "giveGold"

@HiltViewModel
class SampleTakeAndReturnViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getHandedListUseCase: GetHandedListUseCase
) :ViewModel(){

    private val _getHandedListLiveData = MutableLiveData<Resource<List<HandedListDomain>>>()
    val getHandedListLiveData: LiveData<Resource<List<HandedListDomain>>>
        get() = _getHandedListLiveData



    fun getHandedList() {
        viewModelScope.launch {
            getHandedListUseCase(
                localDatabase.getToken().orEmpty()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getHandedListLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getHandedListLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _getHandedListLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}