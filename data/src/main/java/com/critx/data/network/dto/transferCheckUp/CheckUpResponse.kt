package com.critx.data.network.dto.transferCheckUp

import com.critx.domain.model.transferCheckUP.CheckUpDomain

data class CheckUpResponse(
    val data :CheckUpDto
)
data class CheckUpDto(
    val required:List<String>?,
    val not_from_box:List<String>?
)

fun CheckUpDto.asDomain():CheckUpDomain{
    return CheckUpDomain(
        required = required.orEmpty(),
        notFromBox = not_from_box.orEmpty()
    )
}
