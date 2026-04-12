package com.iamkurtgoz.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendFeePaymentNotificationRequest(
    @SerialName("feeId")
    val feeId: String,
)
