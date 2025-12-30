package com.iamkurtgoz.domain.model.request

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RpeSurveyRequest(
    @SerialName("taskId")
    val taskId: String?,
    @SerialName("rating")
    val rating: Int?,
)
