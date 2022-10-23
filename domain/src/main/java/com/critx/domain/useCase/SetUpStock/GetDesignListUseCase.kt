package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import javax.inject.Inject

class GetDesignListUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(token:String,jewelleryType:String)
            =setupStockRepository.getDesignList(token,jewelleryType)
}