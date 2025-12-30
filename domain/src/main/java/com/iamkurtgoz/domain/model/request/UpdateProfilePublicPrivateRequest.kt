package com.iamkurtgoz.domain.model.request

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateProfilePublicPrivateRequest(
    @SerialName("isPrivate")
    val isPrivate: Boolean,
)
