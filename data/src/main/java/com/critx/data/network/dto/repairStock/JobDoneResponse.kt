package com.critx.data.network.dto.repairStock

import com.critx.data.network.dto.collectStock.GoldSmithListDto
import com.critx.data.network.dto.collectStock.asDomain
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import com.critx.domain.model.collectStock.GoldSmithListDomain
import com.critx.domain.model.repairStock.JewelleryTypeIdDomain
import com.critx.domain.model.repairStock.JobDomain
import com.critx.domain.model.repairStock.JobDoneDomain
import com.critx.domain.model.repairStock.RepairJobDomain

data class JobDoneResponse(
    val number_of_job_done:String,
    val data:List<JobDto>
)

fun JobDoneResponse.asDomain():JobDoneDomain{
    return JobDoneDomain(
        number_of_job_done = number_of_job_done,
        data = data.map { it.asDomain() }
    )
}

data class JobDto(
    val id:String,
    val goldsmith_id:GoldSmithListDto,
    val jewellery_type_id:JewelleryTypeIdDto,
    val repair_job_id:RepairJobDto,
    val quantity:String,
    val weight_gm:String,
    val status:String
)
fun JobDto.asDomain():JobDomain{
    return JobDomain(
        id = id,
        goldsmith_id = goldsmith_id.asDomain(),
        jewellery_type_id = jewellery_type_id.asDomain(),
        repair_job_id = repair_job_id.asDomain(),
        quantity,weight_gm, status
    )
}

data class RepairJobDto(
    val id:String,
    val name:String
)
fun RepairJobDto.asDomain(): RepairJobDomain {
    return RepairJobDomain(
        id = id,
        name= name
    )
}

data class JewelleryTypeIdDto(
    val id:String,
    val name:String
)
fun JewelleryTypeIdDto.asDomain():JewelleryTypeIdDomain{
    return JewelleryTypeIdDomain(
        id = id,
        name= name
    )
}
