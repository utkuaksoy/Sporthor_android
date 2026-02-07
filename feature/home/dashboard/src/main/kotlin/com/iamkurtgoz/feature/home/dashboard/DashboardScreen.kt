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
package com.iamkurtgoz.feature.home.dashboard

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.domain.eventbus.impl.DashboardEventBus
import com.iamkurtgoz.domain.model.enums.MenuKeyType.AddDocument
import com.iamkurtgoz.domain.model.enums.MenuKeyType.CoachList
import com.iamkurtgoz.domain.model.enums.MenuKeyType.GenerateClub
import com.iamkurtgoz.domain.model.enums.MenuKeyType.MainMenu
import com.iamkurtgoz.domain.model.enums.MenuKeyType.SporterClub
import com.iamkurtgoz.domain.model.enums.MenuKeyType.TrainingGroup
import com.iamkurtgoz.domain.model.enums.MenuKeyType.TrainingGroupEdit
import com.iamkurtgoz.domain.model.enums.MenuKeyType.TrainingGroupUsers
import com.iamkurtgoz.domain.model.enums.MenuKeyType.UpdateClub
import com.iamkurtgoz.domain.model.enums.MenuKeyType.WebRedirect
import com.iamkurtgoz.domain.model.enums.MenuKeyType.PaymentList

import com.iamkurtgoz.feature.home.dashboard.component.comment.CommentDialog
import com.iamkurtgoz.feature.home.dashboard.domain.model.MenuUIModelItem
import com.iamkurtgoz.feature.home.dashboard.domain.types.MenuClickModel
import kotlinx.coroutines.launch
import com.iamkurtgoz.core.resources.R as resourcesR

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun DashboardScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToStory: (String) -> Unit,
    navigateToMediaViewer: (routeType: HomeScreenMediaViewerScreenNavigateModel) -> Unit,
    navigateToShare: (routeType: HomeScreenShareRouteScreenNavigateModel) -> Unit,
    navigateToWebView: (routeType: HomeScreenWebViewScreenNavigateModel) -> Unit,
    navigateToCalendar: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToCreateTeamScreen: () -> Unit,
    navigateToSelectSportClubScreen: () -> Unit,
    navigateToSelectTeamScreen: (fromGenerateClub: Boolean, fromTrainingGroup: Boolean) -> Unit,
    navigateToSelectTrainingGroupScreen: (fromTrainingGroup: Boolean) -> Unit,
    navigateToCoachListScreen: () -> Unit,
    navigateToPaymentListScreen:() -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentPreferenceState by AppTheme.appPreferences.currentPreferenceState.collectAsStateWithLifecycle(
        initialValue = null,
    )
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
        )
    } else {
        rememberPermissionState(
            permission = "any",
            onPermissionResult = {},
            previewPermissionStatus = PermissionStatus.Granted,
        )
    }

    TrackedScreen("DashboardScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(DashboardScreenContract.Event.Initialize)
    }

    LaunchedEffect(currentPreferenceState?.isAskedNotificationPermission, notificationPermissionState.status.isGranted) {
        if (currentPreferenceState?.isAskedNotificationPermission == false && !notificationPermissionState.status.isGranted) {
            notificationPermissionState.launchPermissionRequest()
        }
    }

    AppTheme.appEventBus.dashboardEventBus.observeEventBus { event ->
        if (event is DashboardEventBus.Event.RefreshHome) {
            scope.launch {
                listState.scrollToItem(0)
            }
        }
        viewModel.setEvent(DashboardScreenContract.Event.UpdateEventBusStatus(event))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is DashboardScreenContract.SideEffect.NavigateUp -> navigateUp()
            is DashboardScreenContract.SideEffect.PopBackStack -> popBackStack()
            is DashboardScreenContract.SideEffect.NavigateToStory -> navigateToStory(event.userId)
            is DashboardScreenContract.SideEffect.NavigateToMediaViewer -> navigateToMediaViewer(event.routeType)
            is DashboardScreenContract.SideEffect.NavigateToShare -> navigateToShare(event.routeType)
            is DashboardScreenContract.SideEffect.NavigateToWebView -> navigateToWebView(event.routeType)
            is DashboardScreenContract.SideEffect.NavigateToCalendar -> navigateToCalendar()
            is DashboardScreenContract.SideEffect.NavigateToNotifications -> navigateToNotifications()
            is DashboardScreenContract.SideEffect.NavigateToCreateTeamScreen -> navigateToCreateTeamScreen()
            is DashboardScreenContract.SideEffect.NavigateToSelectSportClubScreen -> navigateToSelectSportClubScreen()
            is DashboardScreenContract.SideEffect.NavigateToSelectTeamScreen -> navigateToSelectTeamScreen(event.fromGenerateClub, event.fromTrainingGroup)
            is DashboardScreenContract.SideEffect.NavigateToSelectTrainingGroupScreen -> navigateToSelectTrainingGroupScreen(event.fromTrainingGroup)
            is DashboardScreenContract.SideEffect.NavigateToCoachListScreen -> navigateToCoachListScreen()
            is DashboardScreenContract.SideEffect.NavigateToPaymentListScreen -> navigateToPaymentListScreen()

        }
    }

    DashboardScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
        listState = listState,
    )
}

@Composable
private fun DashboardScreenScaffold(
    state: DashboardScreenContract.State,
    setEvent: (DashboardScreenContract.Event) -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ŞU ANKİ MENÜDE mainMenu var mı? yoksa submenu’deyiz
    val isOnSubMenu = state.menuList
        ?.menu
        .orEmpty()
        .any { it.mainMenu == true }
        .not() && !state.isSingleMainMenu

    BackHandler(drawerState.isOpen) {
        if (isOnSubMenu) {
            // önce main menüye dön
            setEvent(DashboardScreenContract.Event.GetMenu)
        } else {
            // zaten main menüdeysek drawer’ı kapat
            scope.launch { drawerState.close() }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                menu = state.menuList?.menu.orEmpty(),
                showBack = isOnSubMenu,
                onBackClick = {
                    setEvent(DashboardScreenContract.Event.GetMenu)
                },
                onMenuItemClick = { menuClickModel ->
                    // scope.launch { drawerState.close() }

                    println(menuClickModel.third)

                    when (menuClickModel.third) {
                        WebRedirect -> {
                            val title = menuClickModel.first
                            val url = menuClickModel.second

                            if (title != null && url != null && menuClickModel.second.hasValidUrl()) {
                                setEvent(
                                    DashboardScreenContract.Event.NavigateToWebView(
                                        routeType = HomeScreenWebViewScreenNavigateModel(
                                            title = title,
                                            url = url,
                                        ),
                                    ),
                                )
                            }
                        }

                        GenerateClub -> {
                            setEvent(
                                DashboardScreenContract.Event.NavigateToSelectTeamScreen(
                                    fromGenerateClub = true,
                                    fromTrainingGroup = false,
                                ),
                            )
                        }

                        UpdateClub -> {
                            setEvent(DashboardScreenContract.Event.NavigateToSelectSportClubScreen)
                        }

                        TrainingGroup -> {
                            setEvent(
                                DashboardScreenContract.Event.NavigateToSelectTeamScreen(
                                    fromGenerateClub = false,
                                    fromTrainingGroup = true,
                                ),
                            )
                        }

                        TrainingGroupEdit -> {
                            setEvent(
                                DashboardScreenContract.Event.NavigateToSelectTrainingGroupScreen(
                                    fromTrainingGroup = true,
                                ),
                            )
                        }

                        MainMenu -> {}
                        AddDocument -> {}
                        SporterClub -> {}
                        TrainingGroupUsers -> {}
                        CoachList -> {
                            setEvent(DashboardScreenContract.Event.NavigateToCoachListScreen)
                        }
                        PaymentList -> {
                            setEvent(DashboardScreenContract.Event.NavigateToPaymentListScreen)
                        }
                        null -> {}
                    }
                },
                onMainMenuClick = { mainMenuItem ->
                    setEvent(
                        DashboardScreenContract.Event.OnMainMenuClick(
                            mainMenuItem = mainMenuItem,
                        ),
                    )
                },
                text = state.menuTitle,
                onClose = {
                    scope.launch { drawerState.close() }
                },
            )
        },
    ) {
        AppThemeScaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.generalColors.backgroundPrimary)
                        .padding(top = AppTheme.configuration.getSafeContentPaddingValues().calculateTopPadding()),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingRegular),
                        onClick = { scope.launch { drawerState.open() } },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menü",
                            tint = AppTheme.colors.generalColors.foregroundPrimary,
                        )
                    }

                    Image(
                        painter = painterResource(resourcesR.drawable.img_sporthor_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .height(32.dp),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        modifier = Modifier,
                        onClick = {
                            setEvent.invoke(DashboardScreenContract.Event.NavigateToNotifications)
                        },
                    ) {
                        Image(
                            painter = painterResource(resourcesR.drawable.img_notification_default),
                            contentDescription = "Menü",
                            modifier = Modifier
                                .height(20.dp),
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .padding(end = AppTheme.spacing.spacingRegular),
                        onClick = {
                            setEvent.invoke(DashboardScreenContract.Event.NavigateToCalendar)
                        },
                    ) {
                        Image(
                            painter = painterResource(resourcesR.drawable.img_calendar_with_badge),
                            contentDescription = "Menü",
                            modifier = Modifier
                                .height(20.dp),
                        )
                    }
                }
            },
        ) { contentPadding ->
            DashboardScreenContent(
                modifier = Modifier
                    .padding(bottom = AppTheme.appHomeSafeAreaPadding.calculateBottomPadding()),
                contentPadding = contentPadding,
                state = state,
                setEvent = setEvent,
                listState = listState,
            )

            state.alertDialogModel?.Alert {
                setEvent.invoke(DashboardScreenContract.Event.DismissDialogs)
            }

            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AppLoadingDialog()
            }

            AnimatedVisibility(
                visible = state.commentDialogShowPostId != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CommentDialog(
                    postId = state.commentDialogShowPostId,
                    onDismissRequest = {
                        setEvent.invoke(DashboardScreenContract.Event.DismissDialogs)
                    },
                )
            }
        }
    }
}

@Composable
fun HomeDrawerContent(
    menu: List<MenuUIModelItem>,
    modifier: Modifier = Modifier,
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuItemClick: (MenuClickModel) -> Unit,
    onMainMenuClick: (MenuUIModelItem) -> Unit,
    text: String,
    onClose: () -> Unit = {},
) {
    var expandedMenuId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.generalColors.backgroundPrimary)
            .padding(top = AppTheme.appHomeSafeAreaPadding.calculateTopPadding()),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text ?: "",
                    color = AppTheme.colors.generalColors.foregroundPrimary,
                    style = AppTheme.typography.subtitleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        horizontal = AppTheme.spacing.spacingMedium,
                        vertical = AppTheme.spacing.spacingSmall,
                    )
                )

                if (!showBack) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kapat",
                            tint = AppTheme.colors.generalColors.foregroundPrimary,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(AppTheme.spacing.spacingSmall))


        }

        if (showBack) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onBackClick() }
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                            horizontal = AppTheme.spacing.spacingMedium,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = AppTheme.colors.generalColors.foregroundPrimary,
                    )
                    Spacer(modifier = Modifier.size(AppTheme.spacing.spacingSmall))
                    Text(
                        text = "Geri",
                        color = AppTheme.colors.generalColors.foregroundPrimary,
                        style = AppTheme.typography.subtitleLarge,
                    )
                }

                Spacer(modifier = Modifier.size(AppTheme.spacing.spacingSmall))

                HorizontalDivider(
                    color = AppTheme.colors.generalColors.foregroundDisabled,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = AppTheme.spacing.spacingMedium),
                )
            }
        }

        items(menu, key = { "${it.menuKey}_${it.name}" }) { menuItem ->
            val itemId = "${menuItem.menuKey}_${menuItem.name}"
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when {
                                // MAIN MENU: sayfayı yeniden yükle / menüyü değiştir
                                menuItem.mainMenu == true -> {
                                    expandedMenuId = null
                                    onMainMenuClick(menuItem)
                                }

                                // Normal menu + subMenu: expand/collapse
                                menuItem.subMenus?.isNotEmpty() == true -> {
                                    expandedMenuId =
                                        if (expandedMenuId == itemId) null else itemId
                                }

                                // Leaf menu: direkt click
                                else -> {
                                    onMenuItemClick(
                                        MenuClickModel(
                                            first = menuItem.name,
                                            second = menuItem.url,
                                            third = menuItem.menuKey,
                                        ),
                                    )
                                }
                            }
                        }
                        .padding(
                            vertical = AppTheme.spacing.spacingSmall,
                            horizontal = AppTheme.spacing.spacingMedium,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    menuItem.iconPath?.takeIf { it.isNotBlank() }?.let { iconUrl ->
                        AppAsyncImageLoader.Load(
                            data = iconUrl,
                            modifier = Modifier
                                .size(AppTheme.dimens.dp24)
                                .padding(end = AppTheme.spacing.spacingSmall),
                        )
                    }

                    Text(
                        text = menuItem.name.orEmpty(),
                        color = AppTheme.colors.generalColors.foregroundPrimary,
                        style = AppTheme.typography.subtitleLarge,
                        modifier = Modifier.weight(1f),
                    )

                    when {
                        // mainMenu ise sadece sağ ok gösterebilirsin (isteğe bağlı)
                        menuItem.mainMenu == true -> {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = AppTheme.colors.generalColors.foregroundPrimary,
                            )
                        }
                        // alt menüsü olan normal menu ise expand/collapse ikonu
                        menuItem.subMenus?.isNotEmpty() == true -> {
                            Icon(
                                imageVector = if (expandedMenuId == itemId)
                                    Icons.Default.KeyboardArrowDown
                                else
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = AppTheme.colors.generalColors.foregroundPrimary,
                            )
                        }
                    }
                }

                // MAIN MENU’ler için burada hiçbir şey göstermiyorsun,
                // sadece normal menu + subMenu’ler için expand alanı:
                if (menuItem.mainMenu != true && expandedMenuId == itemId) {
                    menuItem.subMenus?.forEach { subMenuItem ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = AppTheme.spacing.spacingMedium,
                                    vertical = AppTheme.spacing.spacingSmall,
                                ),
                            shape = AppTheme.shapes.radiusMedium,
                            colors = CardDefaults.cardColors(
                                containerColor = AppTheme.colors.generalColors.backgroundSecondary,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onMenuItemClick(
                                            MenuClickModel(
                                                first = subMenuItem.name,
                                                second = subMenuItem.url,
                                                third = subMenuItem.menuKey,
                                            ),
                                        )
                                    }
                                    .padding(
                                        vertical = AppTheme.spacing.spacingSmall,
                                        horizontal = AppTheme.spacing.spacingMedium,
                                    ),
                            ) {
                                subMenuItem.iconPath?.takeIf { it.isNotBlank() }?.let { iconUrl ->
                                    AppAsyncImageLoader.Load(
                                        data = iconUrl,
                                        modifier = Modifier
                                            .size(AppTheme.dimens.dp20)
                                            .padding(end = AppTheme.spacing.spacingSmall),
                                    )
                                }

                                Text(
                                    text = subMenuItem.name.orEmpty(),
                                    color = AppTheme.colors.generalColors.foregroundPrimary,
                                    style = AppTheme.typography.subtitleSmall,
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    color = AppTheme.colors.generalColors.foregroundDisabled,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = AppTheme.spacing.spacingMedium),
                )
            }
        }
    }
}

private fun String?.hasValidUrl(): Boolean {
    return !this.isNullOrBlank()
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            DashboardScreenScaffold(
                state = DashboardScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
                listState = rememberLazyListState(),
            )
        }
    }
}
