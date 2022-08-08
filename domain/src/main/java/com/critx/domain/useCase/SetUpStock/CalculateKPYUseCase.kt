package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CalculateKPYUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        kyat:Double,
        pae:Double,
        ywae:Double
    )=setupStockRepository.calculateKPYtoGram(token,kyat, pae, ywae)
}