package com.critx.domain.useCase

import com.critx.domain.model.Home
import com.critx.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Home = homeRepository.getHomeData()
}
