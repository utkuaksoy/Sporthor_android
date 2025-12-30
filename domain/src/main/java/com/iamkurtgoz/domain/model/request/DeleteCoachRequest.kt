package com.iamkurtgoz.domain.model.request

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DeleteCoachRequest(
    @SerialName("coachId")
    val coachId: String?,
    @SerialName("trainingGroupId")
    val trainingGroupId: String?,
)
