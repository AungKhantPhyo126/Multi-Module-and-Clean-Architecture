package com.critx.shwemiAdmin.pagingDataSource.pagingRepo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.critx.commonkotlin.util.Resource
import com.critx.data.network.datasource.getErrorString
import com.critx.data.network.datasource.parseError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.getMessage
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.PagingMetaDomain
import com.critx.domain.useCase.orderStock.GetBookMarksUseCase
import kotlinx.coroutines.flow.collectLatest
import org.threeten.bp.LocalDate

class GetBookMarkPagingDataSource(
    private val getBookMarksUseCase: GetBookMarksUseCase,
    private val token: String,
    private val jewelleryType:String
) : PagingSource<Int, BookMarkStockDomain>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookMarkStockDomain> {
        return try {
            val currentPage = params.key ?: 1
            var bookMarkList = mutableListOf<BookMarkStockDomain>()
            var errorMessage = ""
            var metaDomain:PagingMetaDomain? = null
            getBookMarksUseCase(token,jewelleryType,currentPage).collectLatest {
                when(it){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        if (it.data!!.data!!.isNotEmpty()){
                            bookMarkList.addAll(it.data!!.data.orEmpty())
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

    override fun getRefreshKey(state: PagingState<Int, BookMarkStockDomain>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}