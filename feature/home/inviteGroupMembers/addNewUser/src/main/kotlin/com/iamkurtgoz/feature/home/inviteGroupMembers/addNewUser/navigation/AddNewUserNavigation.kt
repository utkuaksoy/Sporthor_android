package com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberAddNewUserRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.homeScreenAddNewUserRouteTypeMap
import com.iamkurtgoz.feature.home.inviteGroupMembers.addNewUser.AddNewUserScreen

fun NavGraphBuilder.addNewUserScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenInviteGroupMemberAddNewUserRoute>(
        typeMap = homeScreenAddNewUserRouteTypeMap,
    ) {
        AddNewUserScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToAddNewUserScreen(
    model: HomeScreenAddNewUserScreenNavigationModel,
    fromTrainingGroup: Boolean = false,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenInviteGroupMemberAddNewUserRoute(
        model = model,
        fromTrainingGroup = fromTrainingGroup,
    )
    this.navigate(route, navOptions)
}
