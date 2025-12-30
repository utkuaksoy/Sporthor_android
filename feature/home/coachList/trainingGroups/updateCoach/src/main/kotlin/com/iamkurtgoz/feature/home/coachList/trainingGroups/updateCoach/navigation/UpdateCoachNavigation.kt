package com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsUpdateCoachScreenRoute
import com.iamkurtgoz.feature.home.coachList.trainingGroups.updateCoach.UpdateCoachScreen

fun NavGraphBuilder.updateCoachScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
) {
    HomeCoachListTrainingGroupsUpdateCoachScreenRoute.composable(
        navGraphBuilder = this,
        content = {
            UpdateCoachScreen(
                navigateUp = navigateUp,
                popBackStack = popBackStack,
                navigateToHome = navigateToHome,
            )
        },
    )
}

fun NavController.navigateToUpdateCoachScreen(clubId: String?, trainingGroupId: String?, navOptions: NavOptions? = null) {
    val route = HomeCoachListTrainingGroupsUpdateCoachScreenRoute.Route(
        clubId = clubId,
        trainingGroupId = trainingGroupId,
    )
    this.navigate(route, navOptions)
}
