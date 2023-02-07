package com.critx.shwemiAdmin.pagingDataSource.pagingRepo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.orderStock.PagingMetaDomain
import com.critx.domain.useCase.orderStock.GetBookMarksUseCase
import com.critx.domain.useCase.orderStock.GetGsNewItemsUseCase
import com.critx.shwemiAdmin.uiModel.orderStock.BookMarkStockUiModel
import com.critx.shwemiAdmin.uiModel.orderStock.asUiModel
import kotlinx.coroutines.flow.collectLatest

class GetGsNewItemsPagingDataSource(
    private val getGsNewItemsUseCase: GetGsNewItemsUseCase,
    private val token: String,
    private val jewelleryType: String

    ) : PagingSource<Int, BookMarkStockUiModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookMarkStockUiModel> {
        return try {
            val currentPage = params.key ?: 1
            var bookMarkList = mutableListOf<BookMarkStockUiModel>()
            var errorMessage = ""
            var metaDomain: PagingMetaDomain? = null
            getGsNewItemsUseCase(token,currentPage,jewelleryType).collectLatest {
                when(it){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        if (it.data!!.data!!.isNotEmpty()){
                            bookMarkList.addAll(it.data!!.data!!.map { it.asUiModel() })
                        }else{
                            bookMarkList = mutableListOf()
                        }
                        metaDomain = it.data!!.meta
                    }
                    is Resource.Error->{
                        errorMessage = it.message.orEmpty()
                    }
                }
            }
            if (errorMessage.isEmpty()) {
                LoadResult.Page(
                    bookMarkList,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (currentPage == metaDomain?.lastPage) null else currentPage + 1
                )
            } else {
                LoadResult.Error(
                    Exception(
                        errorMessage
                    )
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BookMarkStockUiModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}