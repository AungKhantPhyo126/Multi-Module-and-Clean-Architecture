package com.critx.shwemiAdmin.uiModel.setupStock

import com.critx.domain.model.SetupStock.jewelleryCategory.DesignDomain

data class DesignUiModel(
    val id:String,
    val name:String
)
fun DesignDomain.asUiModel():DesignUiModel{
    return DesignUiModel(
        id, name
    )
}

