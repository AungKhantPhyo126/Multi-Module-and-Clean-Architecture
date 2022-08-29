package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import javax.inject.Inject

class GetRelatedCategoryUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(token:String,catId:String)=setupStockRepository.getRelatedCategories(token,catId)
}