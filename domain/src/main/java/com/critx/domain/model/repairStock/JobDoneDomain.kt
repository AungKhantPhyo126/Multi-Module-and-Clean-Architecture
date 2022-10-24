package com.critx.domain.model.repairStock

import com.critx.domain.model.collectStock.GoldSmithListDomain

data class JobDoneDomain(
    val number_of_job_done: String,
    val data: List<JobDomain>
)

data class JobDomain(
    val id:String,
    val goldsmith_id:GoldSmithListDomain,
    val jewellery_type_id:JewelleryTypeIdDomain,
    val repair_job_id:RepairJobDomain,
    val quantity:String,
    val weight_gm:String,
    val status:String
)

data class RepairJobDomain(
    val id:String,
    val name:String
)
data class JewelleryTypeIdDomain(
    val id:String,
    val name:String
)