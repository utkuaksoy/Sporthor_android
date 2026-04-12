package com.iamkurtgoz.domain.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetFeesDetailDomainModel(
    @SerialName("user")
    val user: FeeDetailUserDomainModel?,
    @SerialName("seasons")
    val seasons: List<FeeDetailSeasonDomainModel>?,
)

@Serializable
data class FeeDetailUserDomainModel(
    @SerialName("userId")
    val userId: String?,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("trainingGroupId")
    val trainingGroupId: String?,
    @SerialName("trainingGroupName")
    val trainingGroupName: String?,
    @SerialName("registrationDate")
    val registrationDate: String?,
    @SerialName("profilePhoto")
    val profilePhoto: String? = null,
)

@Serializable
data class FeeDetailSeasonDomainModel(
    @SerialName("seasonId")
    val seasonId: String?,
    @SerialName("seasonName")
    val seasonName: String?,
    @SerialName("fees")
    val fees: List<FeeDetailItemDomainModel>?,
)

@Serializable
data class FeeDetailItemDomainModel(
    @SerialName("id")
    val id: String?,
    @SerialName("categoryId")
    val categoryId: String?,
    @SerialName("categoryName")
    val categoryName: String?,
    @SerialName("periodId")
    val periodId: String?,
    @SerialName("periodName")
    val periodName: String?,
    @SerialName("balance")
    val balance: Double?,
    @SerialName("balanceText")
    val balanceText: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("paymentStatus")
    val paymentStatus: Int?,
    @SerialName("paymentDate")
    val paymentDate: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
)
