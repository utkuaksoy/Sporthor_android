package com.iamkurtgoz.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFeesDomainModel(
    @SerialName("filterType")
    val filterType: Int?,
    @SerialName("isShowPlus")
    val isShowPlus: Boolean? = null,
    @SerialName("fees")
    val fees: List<FeeGroupDomainModel>?,
)

@Serializable
data class FeeGroupDomainModel(
    @SerialName("trainingGroupId")
    val trainingGroupId: String?,
    @SerialName("trainingGroupName")
    val trainingGroupName: String?,
    @SerialName("trainingGroupPhotoUrl")
    val trainingGroupPhotoUrl: String?,
    @SerialName("summary")
    val summary: FeeSummaryDomainModel?,
    @SerialName("users")
    val users: List<FeeUserDomainModel>?,
)

@Serializable
data class FeeSummaryDomainModel(
    @SerialName("completedCount")
    val completedCount: Int?,
    @SerialName("overdueCount")
    val overdueCount: Int?,
)

@Serializable
data class FeeUserDomainModel(
    @SerialName("userId")
    val userId: String?,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("profilePhoto")
    val profilePhoto: String?,
    @SerialName("paymentStatus")
    val paymentStatus: Int?,
    @SerialName("balanceText")
    val balanceText: String?,
    @SerialName("balance")
    val balance: Double?,
    @SerialName("currency")
    val currency: String?,
)
