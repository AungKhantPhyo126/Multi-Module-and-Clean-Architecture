package com.critx.domain.repository

import com.critx.domain.model.Home

interface HomeRepository {
    suspend fun getHomeData():Home
}