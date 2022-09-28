//package com.critx.domain.useCase.collectStock
//
//import com.critx.domain.repository.CollectStockRepository
//import okhttp3.RequestBody
//import javax.inject.Inject
//
//class GetProductIdListUseCase @Inject constructor(
//    private val collectStockRepository: CollectStockRepository
//) {
//    operator fun invoke(token:String,productCodeList:List<RequestBody>) =
//        collectStockRepository.getProductIdList(token, productCodeList)
//}