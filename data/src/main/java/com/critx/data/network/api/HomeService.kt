package com.critx.data.network.api

import com.critx.data.network.responseModel.HomeResponse

interface HomeService {
    suspend fun getHomeApi():HomeResponse
}