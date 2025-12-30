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
package com.iamkurtgoz.feature.home.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenChatRoute
import com.iamkurtgoz.feature.home.chat.ChatScreen

fun NavGraphBuilder.chatScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToNewChatScreen: () -> Unit,
    navigateToMessagingScreen: (isGroup: Boolean, title: String, channelId: String, userId: String) -> Unit,
) {
    composable<HomeScreenChatRoute> {
        ChatScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToNewChatScreen = navigateToNewChatScreen,
            navigateToMessagingScreen = navigateToMessagingScreen,
        )
    }
}

fun NavController.navigateToChatScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenChatRoute
    this.navigate(route, navOptions)
}
