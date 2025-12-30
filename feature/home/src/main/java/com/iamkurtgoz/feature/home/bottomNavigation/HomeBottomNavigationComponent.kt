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

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold

private const val SHADOW_SIZE = -12f

@Composable
internal fun HomeBottomNavigationComponent(
    homeNavController: NavHostController,
    scrollToTop: (Any) -> Unit = { },
) {
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val topLevelRoutes = remember {
        listOf(
            TopLevelRoutes.Dashboard,
            TopLevelRoutes.Search,
            TopLevelRoutes.Share,
            TopLevelRoutes.Chat,
            TopLevelRoutes.Profile,
        )
    }

    if (LocalInspectionMode.current || topLevelRoutes.bottomNavIsActive(navBackStackEntry)) {
        val shadowColors = listOf(
            AppTheme.colors.generalColors.transparent,
            AppTheme.colors.generalColors.textPrimary.copy(alpha = AppDefaults.COMPOSE_COLORS_QUARTER_ALPHA),
            AppTheme.colors.generalColors.textPrimary.copy(alpha = AppDefaults.COMPOSE_COLORS_HALF_ALPHA),
            AppTheme.colors.generalColors.textPrimary,
        )

        NavigationBar(
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = shadowColors,
                            startY = SHADOW_SIZE,
                            endY = size.height,
                        ),
                        topLeft = Offset(0f, SHADOW_SIZE),
                    )
                },
            containerColor = AppTheme.colors.barColors.navigationBarColors.navigationBarContainerColor,
        ) {
            val currentDestination = navBackStackEntry?.destination
            topLevelRoutes.forEach { topLevelRoute ->
                val qualifiedName = topLevelRoute.route::class.qualifiedName ?: ""
                val isSelected = currentDestination?.hierarchy?.any { it.route?.startsWith(qualifiedName) == true } == true
                val iconRes = remember(isSelected) {
                    when (isSelected) {
                        true -> topLevelRoute.selectedIcon
                        false -> topLevelRoute.unSelectedIcon
                    }
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (topLevelRoute == TopLevelRoutes.Share) {
                            homeNavController.navigate(TopLevelRoutes.Share.route)
                        } else {
                            // if current route and clicked route same(re click) so call the scroll to top function
                            if (currentDestination.isRouteInHierarchy(topLevelRoute.route)) {
                                scrollToTop(topLevelRoute)
                            }

                            homeNavController.navigate(topLevelRoute.route) {
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "",
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AppTheme.colors.barColors.navigationBarColors.navigationBarIconSelectedColor,
                        selectedTextColor = AppTheme.colors.barColors.navigationBarColors.navigationBarTextSelectedColor,
                        unselectedIconColor = AppTheme.colors.barColors.navigationBarColors.navigationBarIconUnSelectedColor,
                        unselectedTextColor = AppTheme.colors.barColors.navigationBarColors.navigationBarTextUnSelectedColor,
                        indicatorColor = AppTheme.colors.barColors.navigationBarColors.navigationBarContainerColor,
                    ),
                    alwaysShowLabel = true,
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    val homeNavController = rememberNavController()
    AppTheme {
        AppThemeScaffold(
            bottomBar = {
                HomeBottomNavigationComponent(
                    homeNavController = homeNavController,
                    scrollToTop = { },
                )
            },
        ) { }
    }
}
