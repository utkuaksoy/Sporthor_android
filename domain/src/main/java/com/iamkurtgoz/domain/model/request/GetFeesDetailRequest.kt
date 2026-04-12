package com.iamkurtgoz.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFeesDetailRequest(
    @SerialName("userId")
    val userId: String?,
    @SerialName("trainingGroupId")
    val trainingGroupId: String?,
)
