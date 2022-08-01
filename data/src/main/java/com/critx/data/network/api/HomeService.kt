package com.critx.data.network.api

import com.critx.data.network.dto.HomeResponse

interface HomeService {
    suspend fun getHomeApi():HomeResponse
}