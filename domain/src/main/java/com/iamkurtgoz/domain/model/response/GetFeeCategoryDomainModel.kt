package com.iamkurtgoz.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFeeCategoryDomainModel(
    @SerialName("categories")
    val categories: List<FeeCategoryItemDomainModel>?,
)

@Serializable
data class FeeCategoryItemDomainModel(
    @SerialName("id")
    val id: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("isShowDescription")
    val isShowDescription: Boolean?,
)
