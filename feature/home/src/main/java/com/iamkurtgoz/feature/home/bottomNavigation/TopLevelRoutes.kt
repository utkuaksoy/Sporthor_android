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
package com.iamkurtgoz.feature.home.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.iamkurtgoz.core.navigation.HomeScreenChatRoute
import com.iamkurtgoz.core.navigation.HomeScreenDashboardRoute
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.core.navigation.HomeScreenSearchRoute
import com.iamkurtgoz.core.navigation.HomeScreenShareRoute
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class TopLevelRoutes<T>(
    @StringRes val name: Int,
    @DrawableRes val unSelectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: T,
    val showBottomBar: Boolean = true,
) {
    @Serializable
    data object Dashboard : TopLevelRoutes<HomeScreenDashboardRoute>(
        name = resourcesR.string.homenav_label_dashboard,
        unSelectedIcon = resourcesR.drawable.img_home_button_dashboard_un_selected,
        selectedIcon = resourcesR.drawable.img_home_button_dashboard_selected,
        route = HomeScreenDashboardRoute,
    )

    @Serializable
    data object Search : TopLevelRoutes<HomeScreenSearchRoute>(
        name = resourcesR.string.homenav_label_search,
        unSelectedIcon = resourcesR.drawable.img_home_button_search_un_selected,
        selectedIcon = resourcesR.drawable.img_home_button_search_selected,
        route = HomeScreenSearchRoute,
    )

    @Serializable
    data object Share : TopLevelRoutes<HomeScreenShareRoute>(
        name = resourcesR.string.homenav_label_share,
        unSelectedIcon = resourcesR.drawable.img_home_button_share_un_selected,
        selectedIcon = resourcesR.drawable.img_home_button_share_selected,
        route = HomeScreenShareRoute(
            routeType = HomeScreenShareRouteScreenNavigateModel.CreatePost,
        ),
        showBottomBar = false,
    )

    @Serializable
    data object Chat : TopLevelRoutes<HomeScreenChatRoute>(
        name = resourcesR.string.homenav_label_chat,
        unSelectedIcon = resourcesR.drawable.img_home_button_chat_un_selected,
        selectedIcon = resourcesR.drawable.img_home_button_chat_selected,
        route = HomeScreenChatRoute,
    )

    @Serializable
    data object Profile : TopLevelRoutes<HomeScreenProfileRoute>(
        name = resourcesR.string.homenav_label_profile,
        unSelectedIcon = resourcesR.drawable.img_home_button_profile_un_selected,
        selectedIcon = resourcesR.drawable.img_home_button_profile_selected,
        route = HomeScreenProfileRoute(
            userId = null,
        ),
    )
}
