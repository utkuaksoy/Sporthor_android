package com.iamkurtgoz.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFeesRequest(
    @SerialName("filterType")
    val filterType: Int,
)