package com.iamkurtgoz.domain.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddFeeUsersOrTrainingGroupsRequest(
    @SerialName("usersWithTrainingGroups")
    val usersWithTrainingGroups: List<AddFeeUserWithTrainingGroupRequest>?,
    @SerialName("trainingGroupIds")
    val trainingGroupIds: List<String>?,
    @SerialName("categoryId")
    val categoryId: String?,
    @SerialName("paymentTypeId")
    val paymentTypeId: String?,
    @SerialName("paymentDate")
    val paymentDate: String?,
    @SerialName("paymentAgainCount")
    val paymentAgainCount: Int?,
    @SerialName("amount")
    val amount: Double?,
    @SerialName("description")
    val description: String?,
)

@Serializable
data class AddFeeUserWithTrainingGroupRequest(
    @SerialName("userId")
    val userId: String?,
    @SerialName("trainingGroupId")
    val trainingGroupId: String?,
)
