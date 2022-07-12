package com.critx.data.datasource

import com.critx.data.network.responseModel.HomeResponse

interface HomeNetWorkDatasource {
    suspend fun getHomeData():HomeResponse
}