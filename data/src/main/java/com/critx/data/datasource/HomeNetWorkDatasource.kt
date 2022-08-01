package com.critx.data.datasource

import com.critx.data.network.dto.HomeResponse

interface HomeNetWorkDatasource {
    suspend fun getHomeData():HomeResponse
}