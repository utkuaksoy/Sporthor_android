package com.iamkurtgoz.feature.home.profile.settings.accountSettings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenAccountSettingsScreenRoute
import com.iamkurtgoz.feature.home.profile.settings.accountSettings.AccountSettingsScreen

fun NavGraphBuilder.accountSettingsScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenAccountSettingsScreenRoute> {
        AccountSettingsScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToAccountSettingsScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenAccountSettingsScreenRoute
    this.navigate(route, navOptions)
}
