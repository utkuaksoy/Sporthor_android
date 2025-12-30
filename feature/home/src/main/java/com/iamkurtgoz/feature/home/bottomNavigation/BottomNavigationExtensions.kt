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

import android.os.Build
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute

internal fun <T : Any> NavDestination?.isRouteInHierarchy(route: T) = this?.hierarchy?.any {
    it.route == route::class.qualifiedName
} ?: false

private const val HOME_SCREEN_PROFILE_USER_ID_KEY = "userId"

internal fun <T : Any> List<TopLevelRoutes<out T>>.bottomNavIsActive(navBackStackEntry: NavBackStackEntry?): Boolean {
    var isRouteInHierarchy = false

    // Profile
    navBackStackEntry?.destination?.route
        ?.split(".")
        ?.map {
            if (it.contains("/")) {
                it.split("/").firstOrNull()
            } else {
                it
            }
        }?.firstOrNull { it == HomeScreenProfileRoute::class.simpleName }?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (navBackStackEntry.arguments?.getString(HOME_SCREEN_PROFILE_USER_ID_KEY, null) == null) {
                    isRouteInHierarchy = true
                }
            } else {
                @Suppress("DEPRECATION")
                if (navBackStackEntry.arguments?.getString(HOME_SCREEN_PROFILE_USER_ID_KEY) == null) {
                    isRouteInHierarchy = true
                }
            }
        }

    val current = navBackStackEntry?.destination
    return this.find { current.isRouteInHierarchy(it.route) && it.showBottomBar } != null || isRouteInHierarchy
}
