package com.iamkurtgoz.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddFeeUsersOrTrainingGroupsDomainModel(
    @SerialName("usersProcessed")
    val usersProcessed: Int?,
    @SerialName("totalFeesAdded")
    val totalFeesAdded: Int?,
    @SerialName("message")
    val message: String?,
)
