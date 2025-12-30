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
package com.iamkurtgoz.feature.home.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.iamkurtgoz.core.navigation.HomeScreenCalendarRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.feature.home.calendar.CalendarScreen

fun NavGraphBuilder.calendarScreenNavigation(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToCalendarDetail: (HomeScreenCalendarDetailScreenNavigationModel) -> Unit,
    navigateToAddEvent: (HomeScreenAddEventScreenNavigationModel) -> Unit,
    navigateToSelectEventDrafts: () -> Unit,
) {
    composable<HomeScreenCalendarRoute> {
        CalendarScreen(
            navigateUp = navigateUp,
            popBackStack = popBackStack,
            navigateToCalendarDetail = navigateToCalendarDetail,
            navigateToAddEvent = navigateToAddEvent,
            navigateToSelectEventDrafts = navigateToSelectEventDrafts,
        )
    }
}

fun NavController.navigateToCalendarScreen(navOptions: NavOptions? = null) {
    val route = HomeScreenCalendarRoute
    this.navigate(route, navOptions)
}
