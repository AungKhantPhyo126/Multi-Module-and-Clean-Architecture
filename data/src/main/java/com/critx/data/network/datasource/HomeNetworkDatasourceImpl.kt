package com.critx.data.network.datasource

import com.critx.data.datasource.HomeNetWorkDatasource
import com.critx.data.network.api.HomeService
import com.critx.data.network.responseModel.HomeResponse
import javax.inject.Inject

class HomeNetworkDatasourceImpl @Inject constructor(
    private val homeService: HomeService
):HomeNetWorkDatasource {
    override suspend fun getHomeData():HomeResponse {
        return homeService.getHomeApi()
    }
}