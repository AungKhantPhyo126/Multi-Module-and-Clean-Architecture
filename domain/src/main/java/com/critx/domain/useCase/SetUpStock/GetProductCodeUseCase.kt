package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import javax.inject.Inject

class GetProductCodeUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(token:String)=setupStockRepository.getProductCode(token)
}