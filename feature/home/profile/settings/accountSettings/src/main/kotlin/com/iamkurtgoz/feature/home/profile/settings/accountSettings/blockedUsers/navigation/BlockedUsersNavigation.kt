package com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenBlockedUsersRoute
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers.BlockedUsersScreen

fun NavGraphBuilder.blockedUsersScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenBlockedUsersRoute> {
        BlockedUsersScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToBlockedUsersScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenBlockedUsersRoute
    this.navigate(route, navOptions)
}
