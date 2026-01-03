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
package com.iamkurtgoz.feature.home.calendarDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenCalendarDetailRoute
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.feature.home.calendarDetail.component.CalendarDetailScreenRow
import com.iamkurtgoz.feature.home.calendarDetail.component.RPEBottomSheet
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CalendarDetailScreenContent(
    state: CalendarDetailScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (CalendarDetailScreenContract.Event) -> Unit,
) {
    var showRPEBottomSheet by remember { mutableStateOf(false) }
    var selectedRPERate by remember { mutableStateOf(0) }
    var currentRPEItem by remember { mutableStateOf<CalendarDetailEventUIModelTask?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = AppTheme.spacing.spacingHuge,
        ),
    ) {
        itemsIndexed(
            items = state.calendarDetailEventUIModel?.tasks?.filterNotNull() ?: listOf(),
            key = { index, item ->
                "$index${item.id}"
            },
            itemContent = { index, item ->
                CalendarDetailScreenRow(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                    item = item,
                    onClick = {
                        setEvent.invoke(CalendarDetailScreenContract.Event.NavigateToEditEventScreen(it))
                    },
                    onRPEClick = { clickedItem ->
                        currentRPEItem = clickedItem
                        showRPEBottomSheet = true
                    },
                    onMapClick = { location ->
                        setEvent.invoke(CalendarDetailScreenContract.Event.OnMapClick(location))
                    },
                )
            },
        )
    }

    // RPE Bottom Sheet
    if (showRPEBottomSheet) {
        LaunchedEffect(currentRPEItem) {
            selectedRPERate = currentRPEItem?.rpeRate ?: 0
        }

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { true },
        )

        ModalBottomSheet(
            onDismissRequest = {
                showRPEBottomSheet = false
                selectedRPERate = 0
                currentRPEItem = null
            },
            sheetState = sheetState,
            containerColor = AppTheme.colors.generalColors.foregroundWhite,

        ) {
            RPEBottomSheet(
                selectedRate = selectedRPERate,
                currentTask = currentRPEItem,
                onRateChange = { rate ->
                    selectedRPERate = rate
                },
                onSaveClick = {
                    currentRPEItem?.let { item ->
                        setEvent.invoke(CalendarDetailScreenContract.Event.RpeSurvey(item.id ?: "", selectedRPERate))
                    }
                    showRPEBottomSheet = false
                    selectedRPERate = 0
                    currentRPEItem = null
                },
                onDismiss = {
                    showRPEBottomSheet = false
                    selectedRPERate = 0
                    currentRPEItem = null
                },
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CalendarDetailScreenContent(
                state = CalendarDetailScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenCalendarDetailRoute(
                        model = HomeScreenCalendarDetailScreenNavigationModel(
                            selectedDate = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
