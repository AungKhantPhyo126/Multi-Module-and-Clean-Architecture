package com.critx.domain.useCase.transferCheckUp

import com.critx.domain.repository.TransferCheckUpRepository
import javax.inject.Inject

class CheckUpUseCase @Inject constructor(
    private val transferCheckUpRepository: TransferCheckUpRepository
) {
    operator fun invoke(token:String,boxCode:String,productIdList:List<String>)=transferCheckUpRepository.checkUp(token, boxCode, productIdList)
}