package com.iamkurtgoz.feature.home.coachList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListScreenRoute
import com.iamkurtgoz.feature.home.coachList.CoachListScreen

fun NavGraphBuilder.coachListScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToTrainingGroups: (clubId: String?) -> Unit,
) {
    HomeCoachListScreenRoute.composable(
        navGraphBuilder = this,
        content = {
            CoachListScreen(
                navigateUp = navigateUp,
                popBackStack = popBackStack,
                navigateToTrainingGroups = navigateToTrainingGroups,
            )
        },
    )
}

fun NavController.navigateToCoachListScreen(navOptions: NavOptions? = null) {
    val route = HomeCoachListScreenRoute.Route
    this.navigate(route, navOptions)
}
