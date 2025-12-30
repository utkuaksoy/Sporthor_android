package com.iamkurtgoz.feature.home.editTeam.selectBranch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenEditTeamSelectBranchRoute
import com.iamkurtgoz.feature.home.editTeam.selectBranch.SelectBranchScreen

fun NavGraphBuilder.selectEditTeamBranchScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenEditTeamSelectBranchRoute> {
        SelectBranchScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToEditTeamSelectBranchScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenEditTeamSelectBranchRoute
    this.navigate(route, navOptions)
}
