package com.iamkurtgoz.domain.model.response

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GetRecomendedGroupNamesDomainModel(
    @SerialName("names")
    val names: List<String?>?,
)
