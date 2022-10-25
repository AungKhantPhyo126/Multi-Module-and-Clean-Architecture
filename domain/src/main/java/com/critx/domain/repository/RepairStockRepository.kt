package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.repairStock.JobDoneDomain
import com.critx.domain.model.repairStock.RepairJobDomain
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface RepairStockRepository {
     fun getJobDoneData(
        token: String,
        goldSmithId: String,
    ): Flow<Resource<JobDoneDomain>>

    fun getRepairJob(
        token: String,
        jewelleryTypeId: String,
    ): Flow<Resource<List<RepairJobDomain>>>


     fun assignGoldSmith(
        token: String,
        goldSmithId: RequestBody,
        jewelleryTypeId: RequestBody,
        repairJobId: RequestBody,
        quantity: RequestBody,
        weightGm: RequestBody,
    ): Flow<Resource<SimpleData>>


     fun chargeRepairSTock(
        token: String,
        amount: RequestBody,
        repairStockList: List<RequestBody>,
    ):Flow<Resource<SimpleData>>
}