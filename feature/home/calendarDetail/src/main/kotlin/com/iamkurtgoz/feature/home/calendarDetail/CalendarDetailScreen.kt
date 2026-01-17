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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
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
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenCalendarDetailRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.calendarDetail.HomeScreenCalendarDetailScreenNavigationModel
import com.iamkurtgoz.core.navigation.model.home.editEvent.HomeScreenEditEventScreenNavigationModel
import com.iamkurtgoz.domain.eventbus.impl.CalendarEventBus
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.DayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun CalendarDetailScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToAddEvent: (HomeScreenAddEventScreenNavigationModel) -> Unit,
    navigateToEditEventScreen: (HomeScreenEditEventScreenNavigationModel) -> Unit,
    navigateToSelectEventDrafts: () -> Unit,
    viewModel: CalendarDetailViewModel = hiltViewModel(),
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    TrackedScreen("CalendarDetailScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(CalendarDetailScreenContract.Event.Initialize)
        withContext(Dispatchers.Main) {
            viewModel.setEvent(CalendarDetailScreenContract.Event.SyncScrollState)
        }
    }

    AppTheme.appEventBus.calendarEventBus.observeEventBus { event ->
        when (event) {
            is CalendarEventBus.Event.Refresh -> {
                viewModel.setEvent(CalendarDetailScreenContract.Event.RefreshCalendarDetail)
            }
        }
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is CalendarDetailScreenContract.SideEffect.NavigateUp -> navigateUp()
            is CalendarDetailScreenContract.SideEffect.PopBackStack -> popBackStack()
            is CalendarDetailScreenContract.SideEffect.NavigateToAddEvent -> navigateToAddEvent(event.model)
            is CalendarDetailScreenContract.SideEffect.SyncScrollState -> {
                if (state.selectedDate != null && state.days.isNotEmpty() && !lazyListState.isScrollInProgress) {
                    val index = state.days.indexOfFirst { it.localDate == state.selectedDate }
                    if (index != -1) {
                        lazyListState.scrollToItem(index)
                    }
                }
            }
            is CalendarDetailScreenContract.SideEffect.NavigateToSelectEventDrafts -> navigateToSelectEventDrafts()
            is CalendarDetailScreenContract.SideEffect.NavigateToEditEventScreen -> navigateToEditEventScreen(event.model)
            is CalendarDetailScreenContract.SideEffect.NavigateToMap -> {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse("geo:${event.location.lat},${event.location.lng}?q=${event.location.lat},${event.location.lng}(${event.location.title})"),
                )
                context.startActivity(intent)
            }
        }
    }

    CalendarDetailScreenScaffold(
        state = state,
        lazyListState = lazyListState,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun CalendarDetailScreenScaffold(
    state: CalendarDetailScreenContract.State,
    lazyListState: LazyListState,
    setEvent: (CalendarDetailScreenContract.Event) -> Unit,
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
                            setEvent.invoke(CalendarDetailScreenContract.Event.NavigateUp)
                        }

                        AppToolbarFields.Title(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = state.title.plus(" ${state.selectedDate}"), // TODO: Localize
                            titleTextColor = AppTheme.colors.generalColors.foregroundWhite,
                        )
                    }

                    LazyRow(
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        state = lazyListState,
                    ) {
                        items(state.days.size) {
                            val item = state.days[it]
                            Column(
                                modifier = Modifier
                                    .padding(bottom = AppTheme.spacing.spacingMedium)
                                    .width((dynamicWidth - 32.dp) / AppDefaults.SEVEN)
                                    .height(64.dp)
                                    .ifTrue(item.localDate == state.selectedDate) {
                                        background(
                                            color = AppTheme.colors.generalColors.foregroundWhite.copy(
                                                alpha = 0.1f,
                                            ),
                                            shape = AppTheme.shapes.radiusMedium,
                                        )
                                    }
                                    .clip(AppTheme.shapes.radiusMedium)
                                    .clickable {
                                        val event = CalendarDetailScreenContract.Event.SetSelectedDate(localDate = item.localDate)
                                        setEvent.invoke(event)
                                    },
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = item.dayLetter,
                                    style = AppTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                    ),
                                    color = AppTheme.colors.generalColors.foregroundWhite,
                                )

                                Text(
                                    text = item.dayOfMonth.toString(),
                                    style = AppTheme.typography.heading06,
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
                    setEvent.invoke(CalendarDetailScreenContract.Event.ShowSelectAddEventTypeDialog)
                },
            )
        },
    ) { padding ->
        CalendarDetailScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(CalendarDetailScreenContract.Event.DismissDialogs)
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
                setEvent.invoke(CalendarDetailScreenContract.Event.DismissDialogs)
            },
            addNewEventClick = {
                setEvent.invoke(CalendarDetailScreenContract.Event.DismissDialogs)
                setEvent.invoke(CalendarDetailScreenContract.Event.NavigateToAddEvent(state.selectedDate))
            },
            selectSavedEventsClick = {
                setEvent.invoke(CalendarDetailScreenContract.Event.DismissDialogs)
                setEvent.invoke(CalendarDetailScreenContract.Event.NavigateToSelectEventDrafts)
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    val locale = Locale.getDefault()
    val days: MutableList<DayItem> = mutableListOf()
    for (i in 0..60) {
        val today = LocalDate.now().plusDays(i.toLong())
        val letter = today.dayOfWeek
            .getDisplayName(TextStyle.NARROW_STANDALONE, locale)
            .uppercase(locale)
        days += DayItem(
            localDate = today,
            dayLetter = letter,
            dayOfMonth = today.dayOfMonth,
        )
    }
    AppTheme {
        AppThemeSurface {
            CalendarDetailScreenScaffold(
                state = CalendarDetailScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenCalendarDetailRoute(
                        model = HomeScreenCalendarDetailScreenNavigationModel(
                            selectedDate = LocalDate.now(),
                        ),
                    ),
                    days = days,
                ),
                lazyListState = rememberLazyListState(),
                setEvent = { },
            )
        }
    }
}
