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
package com.iamkurtgoz.feature.home.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.feature.home.profile.ProfileScreen

fun NavGraphBuilder.profileScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToUserRelation: (userRelationFollowingCount: Int?, userRelationFollowerCount: Int?, userName: String, userId: String) -> Unit,
    navigateToMessagingScreen: (isGroup: Boolean, title: String, channelId: String, userId: String) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToPostDetail: (userId: String?, index: Int?) -> Unit,
) {
    composable<HomeScreenProfileRoute> {
        ProfileScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToEditProfile = navigateToEditProfile,
            navigateToUserRelation = navigateToUserRelation,
            navigateToMessagingScreen = navigateToMessagingScreen,
            navigateToSetting = navigateToSetting,
            navigateToPostDetail = navigateToPostDetail,
        )
    }
}

fun NavController.navigateToProfileScreen(userId: String?, navOptions: NavOptions? = null) {
    val route = HomeScreenProfileRoute(
        userId = userId,
    )
    this.navigate(route, navOptions)
}
