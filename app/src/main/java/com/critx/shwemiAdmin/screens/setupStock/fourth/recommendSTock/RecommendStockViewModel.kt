package com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.GetJewelleryCategoryUseCase
import com.critx.domain.useCase.SetUpStock.GetRelatedCategoryUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getRelatedCategoryUseCase: GetRelatedCategoryUseCase
) : ViewModel() {
    private val _recommendCatListLiveData = MutableLiveData<Resource<MutableList<JewelleryCategoryUiModel?>>>()
    val recommendCatListLiveData: LiveData<Resource<MutableList<JewelleryCategoryUiModel?>>>
        get() = _recommendCatListLiveData


    fun getRelatedCategories(catId:String){
        viewModelScope.launch {
            getRelatedCategoryUseCase(localDatabase.getToken().orEmpty(),catId).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _recommendCatListLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
//                        categoryList.addAll(it.data!!.map { it.asUiModel() })
                        _recommendCatListLiveData.value=Resource.Success(it.data!!.map { it.asUiModel() } as MutableList<JewelleryCategoryUiModel?>)
                    }
                    is Resource.Error->{
                        _recommendCatListLiveData.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

}