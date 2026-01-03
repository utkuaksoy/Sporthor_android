/*
 * Copyright 2024 Sporthor Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iamkurtgoz.feature.home.inviteGroupMembers.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenInviteGroupMemberRoute
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenAddNewUserScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.HomeScreenInviteGroupMemberScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.inviteGroupMember.homeScreenInviteGroupMemberRouteTypeMap
import com.iamkurtgoz.feature.home.inviteGroupMembers.InviteGroupMembersScreen

fun NavGraphBuilder.inviteGroupMembersScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSelectGroup: () -> Unit,
    navigateToAddNewUserScreen: (HomeScreenAddNewUserScreenNavigationModel, fromTrainingGroup: Boolean) -> Unit,
) {
    composable<HomeScreenInviteGroupMemberRoute>(
        typeMap = homeScreenInviteGroupMemberRouteTypeMap,
    ) {
        InviteGroupMembersScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToHome = navigateToHome,
            navigateToSelectGroup = navigateToSelectGroup,
            navigateToAddNewUserScreen = navigateToAddNewUserScreen,
        )
    }
}

fun NavController.navigateToInviteGroupMembersScreen(
    model: HomeScreenInviteGroupMemberScreenNavigationModel,
    fromTrainingGroup: Boolean = false,
    navOptions: NavOptions? = null,
) {
    val route = HomeScreenInviteGroupMemberRoute(
        model = model,
        fromTrainingGroup = fromTrainingGroup,
    )
    this.navigate(route, navOptions)
}
