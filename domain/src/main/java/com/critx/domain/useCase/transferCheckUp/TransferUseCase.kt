package com.critx.domain.useCase.transferCheckUp

import com.critx.domain.repository.TransferCheckUpRepository
import javax.inject.Inject

class TransferUseCase@Inject constructor(
    private val transferCheckUpRepository: TransferCheckUpRepository
) {
    operator fun invoke(token:String,boxCode:String,productIdList:List<String>, rfidCode : HashMap<String,String>)
    =transferCheckUpRepository.transfer(token, boxCode, productIdList,rfidCode)
}