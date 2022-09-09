package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.RequestBody
import javax.inject.Inject

class DeleteJewelleryCategoryUseCase@Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        method: RequestBody,
        catId: String
    ) = setupStockRepository.deleteJewelleryCategory(
        token,
        method,
        catId
    )
}