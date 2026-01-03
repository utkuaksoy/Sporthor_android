package com.iamkurtgoz.core.navigation.model.home.inviteGroupMember

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberAddNewUserRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import com.iamkurtgoz.domain.serializer.Base64StringSerializer
import com.iamkurtgoz.domain.serializer.EncodedUrlSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Keep
@Serializable
@Parcelize
data class HomeScreenAddNewUserScreenNavigationModel(
    val isEdit: Boolean = false,
    @Serializable(Base64StringSerializer::class) val clubId: String?,
    @Serializable(Base64StringSerializer::class) val clubName: String?,
    @Serializable(EncodedUrlSerializer::class) val clubLogo: String?,
    @Serializable(Base64StringSerializer::class) val groupId: String?,
    @Serializable(Base64StringSerializer::class) val groupName: String?,
    val users: List<HomeScreenAddNewUserScreenNavigationModelUser> = listOf(),
    val coaches: List<HomeScreenAddNewUserScreenNavigationModelUser> = listOf(),
) : Parcelable

@Keep
@Serializable
@Parcelize
data class HomeScreenAddNewUserScreenNavigationModelUser(
    @Serializable(Base64StringSerializer::class) val id: String?,
    @Serializable(Base64StringSerializer::class) val imageUrl: String?,
    val isCurrentUser: Boolean?,
    val isFollow: Boolean?,
    @Serializable(Base64StringSerializer::class) val name: String?,
    @Serializable(Base64StringSerializer::class) val summary: String?,
    @Serializable(Base64StringSerializer::class) val username: String?,
) : Parcelable

val homeScreenAddNewUserRouteTypeMap = mapOf(
    typeOf<HomeScreenAddNewUserScreenNavigationModel>() to CustomNavType(
        HomeScreenAddNewUserScreenNavigationModel::class.java,
        HomeScreenAddNewUserScreenNavigationModel.serializer(),
    ),
)

fun SavedStateHandle.toHomeScreenAddNewUserRouteTypeMap(): HomeScreenInviteGroupMemberAddNewUserRoute {
    return this.toRoute<HomeScreenInviteGroupMemberAddNewUserRoute>(
        typeMap = homeScreenAddNewUserRouteTypeMap,
    )
}
