package com.critx.shwemiAdmin.pagingDataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroupDomain
import com.critx.domain.useCase.SetUpStock.GetJewelleryGroupUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException
import javax.inject.Inject

class JewelleryGroupPagingSource(
    private val getJewelleryGroupUseCase: GetJewelleryGroupUseCase,
    private val localDatabase: LocalDatabase
):PagingSource<Int,JewelleryGroup>(){
    override fun getRefreshKey(state: PagingState<Int, JewelleryGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JewelleryGroup> {
        val currentPageNumber = params.key?:1
        return try {
            var response : JewelleryGroupDomain? = null
            getJewelleryGroupUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when(it){
                    is Resource.Loading->{
                        it.message
                    }
                    is Resource.Success->{
                        response =it.data!!
                    }
                    is Resource.Error->{
                        it.message
                    }
                }
            }
            val jewelleryGroup =response!!
            LoadResult.Page(
                data = jewelleryGroup.data,
                prevKey = if (currentPageNumber == 1) null else currentPageNumber-1,
                nextKey =if (currentPageNumber == response?.meta?.last_page) null else currentPageNumber + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}
