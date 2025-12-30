package com.iamkurtgoz.feature.home.createTeam.selectBranch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenCreateTeamSelectBranchRoute
import com.iamkurtgoz.feature.home.createTeam.selectBranch.SelectBranchScreen

fun NavGraphBuilder.selectCreateTeamBranchScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable<HomeScreenCreateTeamSelectBranchRoute> {
        SelectBranchScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
        )
    }
}

fun NavController.navigateToCreateTeamSelectBranchScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenCreateTeamSelectBranchRoute
    this.navigate(route, navOptions)
}
