package com.critx.shwemiAdmin.screens.orderStock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.useCase.SetUpStock.GetJewelleryTypeUseCase
import com.critx.domain.useCase.orderStock.GetBookMarksUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.pagingDataSource.pagingRepo.GetBookMarkPagingDataSource
import com.critx.shwemiAdmin.uiModel.orderStock.BookMarkStockUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getBookMarksUseCase: GetBookMarksUseCase,
    private val getJewelleryTypeUseCase: GetJewelleryTypeUseCase
):ViewModel() {
    private var _jewelleryTypeLiveData= MutableLiveData<Resource<List<JewelleryTypeUiModel>>>()
    val jewelleryTypeLiveData : LiveData<Resource<List<JewelleryTypeUiModel>>>
        get() = _jewelleryTypeLiveData



    fun getBookMarks(jewelleryType:String): LiveData<PagingData<BookMarkStockUiModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            initialKey = 1,
            pagingSourceFactory = {
                GetBookMarkPagingDataSource(
                    getBookMarksUseCase,
                    localDatabase.getToken().orEmpty(),
                    jewelleryType
                )
            }
        ).liveData
    }

    fun getBookMarksLiveData(jewelleryType:String): LiveData<PagingData<BookMarkStockUiModel>> {
        return getBookMarks(jewelleryType).cachedIn(viewModelScope)
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

}