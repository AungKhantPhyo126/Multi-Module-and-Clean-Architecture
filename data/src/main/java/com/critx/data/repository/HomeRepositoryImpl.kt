package com.critx.data.repository

import com.critx.data.datasource.HomeNetWorkDatasource
import com.critx.data.network.dto.asDomain
import com.critx.domain.model.Home
import com.critx.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeNetWorkDatasource: HomeNetWorkDatasource): HomeRepository {
    override suspend fun getHomeData(): Home {
        return homeNetWorkDatasource.getHomeData().asDomain()
    }
}