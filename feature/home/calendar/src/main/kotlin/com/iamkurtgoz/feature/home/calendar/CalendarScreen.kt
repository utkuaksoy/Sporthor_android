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
package com.iamkurtgoz.feature.home.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.SelectAddEventTypeDialog
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.domain.eventbus.impl.CalendarEventBus
import com.iamkurtgoz.core.resources.R as resourcesR
import java.time.LocalDate

@Composable
internal fun CalendarScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToCalendarDetail: (HomeScreenCalendarDetailScreenNavigationModel) -> Unit,
    navigateToAddEvent: (HomeScreenAddEventScreenNavigationModel) -> Unit,
    navigateToSelectEventDrafts: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("CalendarScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(CalendarScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.calendarEventBus.observeEventBus { event ->
        when (event) {
            is CalendarEventBus.Event.Refresh -> {
                viewModel.setEvent(CalendarScreenContract.Event.RefreshCalendar)
            }
        }
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CalendarScreenContract.SideEffect.NavigateUp -> navigateUp()
            is CalendarScreenContract.SideEffect.PopBackStack -> popBackStack()
            is CalendarScreenContract.SideEffect.NavigateToCalendarDetail -> navigateToCalendarDetail(event.model)
            is CalendarScreenContract.SideEffect.NavigateToAddEvent -> navigateToAddEvent(event.model)
            is CalendarScreenContract.SideEffect.NavigateToSelectEventDrafts -> navigateToSelectEventDrafts()
        }
    }

    CalendarScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun CalendarScreenScaffold(
    state: CalendarScreenContract.State,
    setEvent: (CalendarScreenContract.Event) -> Unit,
) {
    val density: Density = LocalDensity.current
    var dynamicHeight by remember { mutableStateOf(AppDefaults.ZERO.dp) }
    var dynamicWidth by remember { mutableStateOf(AppDefaults.ZERO.dp) }

    AppThemeScaffold(
        modifier = Modifier
            .onGloballyPositioned {
                dynamicWidth = with(density) {
                    it.size.width.toDp()
                }
            },
        topBar = {
            Box(
                modifier = Modifier,
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_calendar_top),
                    contentDescription = "background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dynamicHeight),
                    contentScale = ContentScale.Crop,
                )

                Column(
                    modifier = Modifier
                        .onGloballyPositioned {
                            dynamicHeight = with(density) {
                                it.size.height.toDp()
                            }
                        }
                        .padding(top = AppTheme.configuration.getSafeContentPaddingValues().calculateTopPadding())
                        .fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        AppToolbarFields.NavigateIcon(
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            iconTint = AppTheme.colors.generalColors.foregroundWhite,
                        ) {
                            setEvent.invoke(CalendarScreenContract.Event.NavigateUp)
                        }

                        AppToolbarFields.Title(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "Takvim", // TODO: Localize
                            titleTextColor = AppTheme.colors.generalColors.foregroundWhite,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingRegular),
                            onClick = {
                                setEvent.invoke(CalendarScreenContract.Event.PreviousMonth)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Menü",
                                tint = AppTheme.colors.generalColors.foregroundWhite,
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp48),
                            )
                        }

                        Text(
                            text = state.title,
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingMedium),
                            style = AppTheme.typography.heading03,
                            color = AppTheme.colors.generalColors.foregroundWhite,
                        )

                        IconButton(
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingRegular),
                            onClick = {
                                setEvent.invoke(CalendarScreenContract.Event.NextMonth)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Menü",
                                tint = AppTheme.colors.generalColors.foregroundWhite,
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp48),
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        items(state.titles.size) {
                            Box(
                                modifier = Modifier
                                    .width((dynamicWidth - 32.dp) / state.titles.size)
                                    .height(48.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = state.titles[it],
                                    style = AppTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                    color = AppTheme.colors.generalColors.foregroundWhite,
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            AppCircleButton.PrimaryLarge(
                icon = resourcesR.drawable.img_plus,
                onClick = {
                    setEvent.invoke(CalendarScreenContract.Event.ShowSelectAddEventTypeDialog)
                },
            )
        }
    ) { padding ->
        CalendarScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(CalendarScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }

        SelectAddEventTypeDialog(
            showSheet = state.showSelectAddEventTypeDialog,
            onDismissRequest = {
                setEvent.invoke(CalendarScreenContract.Event.DismissDialogs)
            },
            addNewEventClick = {
                setEvent.invoke(CalendarScreenContract.Event.DismissDialogs)
                setEvent.invoke(CalendarScreenContract.Event.NavigateToAddEvent(LocalDate.now()))
            },
            selectSavedEventsClick = {
                setEvent.invoke(CalendarScreenContract.Event.DismissDialogs)
                setEvent.invoke(CalendarScreenContract.Event.NavigateToSelectEventDrafts)
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            CalendarScreenScaffold(
                state = CalendarScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
