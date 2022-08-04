package com.critx.shwemiAdmin.pagingDataSource.pagingRepo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.useCase.SetUpStock.GetJewelleryGroupUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.pagingDataSource.JewelleryGroupPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JewelleryGroupPagingRepo @Inject constructor(
    private val getJewelleryGroupUseCase: GetJewelleryGroupUseCase,
    private val localDatabase: LocalDatabase
) {
    fun getJewelleryGroupPaging(): Flow<PagingData<JewelleryGroup>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                JewelleryGroupPagingSource(getJewelleryGroupUseCase, localDatabase)
            }
        ).flow
    }
}