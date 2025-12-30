package com.iamkurtgoz.feature.home.profile.settings.aboutUs.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenProfileSettingsAboutUsRoute
import com.iamkurtgoz.feature.home.profile.settings.aboutUs.AboutUsScreen

fun NavGraphBuilder.profileSettingAboutUsScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenProfileSettingsAboutUsRoute> {
        AboutUsScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToAboutUsScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenProfileSettingsAboutUsRoute
    this.navigate(route, navOptions)
}
