package com.iamkurtgoz.feature.home.coachList.trainingGroups.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.screenRoute.HomeCoachListTrainingGroupsScreenRoute
import com.iamkurtgoz.feature.home.coachList.trainingGroups.TrainingGroupsScreen

fun NavGraphBuilder.trainingGroupsScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToUpdateCoachScreen: (clubId: String?, trainingGroupId: String?) -> Unit,
    navigateToInviteGroupMembersScreen: (model: HomeScreenInviteGroupMemberScreenNavigationModel) -> Unit,
) {
    HomeCoachListTrainingGroupsScreenRoute.composable(
        navGraphBuilder = this,
        content = {
            TrainingGroupsScreen(
                navigateUp = navigateUp,
                popBackStack = popBackStack,
                navigateToUpdateCoachScreen = navigateToUpdateCoachScreen,
                navigateToInviteGroupMembersScreen = navigateToInviteGroupMembersScreen,
            )
        },
    )
}

fun NavController.navigateToTrainingGroupsScreen(clubId: String?, navOptions: NavOptions? = null) {
    val route = HomeCoachListTrainingGroupsScreenRoute.Route(
        clubId = clubId,
    )
    this.navigate(route, navOptions)
}
