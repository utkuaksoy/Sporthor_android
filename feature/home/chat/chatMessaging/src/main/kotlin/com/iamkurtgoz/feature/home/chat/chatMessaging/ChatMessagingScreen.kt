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
package com.iamkurtgoz.feature.home.chat.chatMessaging

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.fileSelectDialog.FileSelectDialog
import com.iamkurtgoz.core.commonui.component.fileSelectDialog.FileSelectDialogFileType
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.commonui.state.keyboardVisibility
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.extension.imeAndStatusBarPadding
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun ChatMessagingScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToMediaViewer: (routeType: HomeScreenMediaViewerScreenNavigateModel) -> Unit,
    navigateToChatMessagingDetailUser: (userId: String?, groupId: String?) -> Unit,
    navigateToChatMessagingDetailGroup: (userId: String?, groupId: String?) -> Unit,
    viewModel: ChatMessagingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isKeyboardShow by keyboardVisibility()
    val lazyListState: LazyListState = rememberLazyListState()
    val localKeyboardController = LocalSoftwareKeyboardController.current
    val chatMessageFocusRequester = remember { FocusRequester() }

    TrackedScreen("ChatMessagingScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(ChatMessagingScreenContract.Event.Initialize)
    }

    LaunchedEffect(isKeyboardShow) {
        viewModel.setEvent(ChatMessagingScreenContract.Event.SetKeyboardShow(isKeyboardShow))
    }

    AppTheme.appEventBus.chatMessagingEventBus.observeEventBus {
        viewModel.setEvent(ChatMessagingScreenContract.Event.UpdateEventBusStatus(it))
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is ChatMessagingScreenContract.SideEffect.NavigateUp -> navigateUp()
            is ChatMessagingScreenContract.SideEffect.PopBackStack -> popBackStack()
            is ChatMessagingScreenContract.SideEffect.ScrollToBottom -> {
                lazyListState.animateScrollToItem(AppDefaults.ZERO)
            }

            is ChatMessagingScreenContract.SideEffect.ShowKeyboard -> {
                chatMessageFocusRequester.requestFocus()
                localKeyboardController?.show()
            }

            is ChatMessagingScreenContract.SideEffect.HideKeyboard -> {
                chatMessageFocusRequester.freeFocus()
                localKeyboardController?.hide()
            }

            is ChatMessagingScreenContract.SideEffect.NavigateToMediaViewer -> {
                navigateToMediaViewer(event.routeType)
            }

            is ChatMessagingScreenContract.SideEffect.NavigateToChatMessagingDetailUser -> {
                navigateToChatMessagingDetailUser(event.userId, event.groupId)
            }

            is ChatMessagingScreenContract.SideEffect.NavigateToChatMessagingDetailGroup -> {
                navigateToChatMessagingDetailGroup(event.userId, event.groupId)
            }
        }
    }

    ChatMessagingScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
        lazyListState = lazyListState,
        chatMessageFocusRequester = chatMessageFocusRequester,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatMessagingScreenScaffold(
    state: ChatMessagingScreenContract.State,
    setEvent: (ChatMessagingScreenContract.Event) -> Unit,
    lazyListState: LazyListState,
    chatMessageFocusRequester: FocusRequester,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = {
                            setEvent.invoke(ChatMessagingScreenContract.Event.NavigateUp)
                        },
                    )
                },
                centerContent = {
                    AppToolbarFields.TitleWithSubTitle(
                        text = state.route.title,
                        subText = if (state.isTyping) "Yazıyor.." else "", // TODO: Localize
                        modifier = Modifier
                            .clickable {
                                if (!state.route.isGroup) {
                                    setEvent.invoke(ChatMessagingScreenContract.Event.NavigateToChatMessagingDetailUser(userId = state.route.userId, groupId = state.route.channelId))
                                } else {
                                    setEvent.invoke(ChatMessagingScreenContract.Event.NavigateToChatMessagingDetailGroup(userId = state.route.userId, groupId = state.route.channelId))
                                }
                            },
                    )
                },
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .imeAndStatusBarPadding(isKeyboardShow = state.isKeyboardShow),
            ) {

                Row(
                    modifier = Modifier
                        .height(AppTheme.dimens.dp64),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppCircleButton.OutlineLarge(
                        icon = if (state.isShowFileSelectDialog) resourcesR.drawable.img_keyboard else resourcesR.drawable.img_plus,
                        onClick = {
                            val event = ChatMessagingScreenContract.Event.SetShowFileSelectDialogState(
                                isShowFileSelectDialog = !state.isShowFileSelectDialog,
                            )
                            setEvent.invoke(event)
                        },
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingMedium),
                    )

                    AppTextField.MessageField(
                        modifier = Modifier
                            .focusRequester(focusRequester = chatMessageFocusRequester)
                            .padding(start = AppTheme.spacing.spacingSmall)
                            .padding(end = AppTheme.spacing.spacingMedium),
                        value = state.textMessage.value,
                        onValueChange = {
                            setEvent.invoke(ChatMessagingScreenContract.Event.SetTextMessage(it))
                        },
                        trailingIcon = if (state.textMessage.isEmpty) resourcesR.drawable.img_chat_message_send_disabled else resourcesR.drawable.img_chat_message_send_enabled,
                        trailingIconClick = {
                            setEvent.invoke(ChatMessagingScreenContract.Event.SendMessage)
                        },
                    )
                }

                if (state.isShowFileSelectDialog) {
                    FileSelectDialog(
                        onSelectedFileCallback = { path, extension, type ->
                            val signalRMessageType = when (type) {
                                FileSelectDialogFileType.IMAGE -> SignalRMessageType.IMAGE
                                FileSelectDialogFileType.VIDEO -> SignalRMessageType.VIDEO
                                FileSelectDialogFileType.DOCUMENT -> SignalRMessageType.FILE
                                else -> null
                            }
                            setEvent.invoke(ChatMessagingScreenContract.Event.SendSelectedFile(path, extension, signalRMessageType))
                        },
                    )
                }
            }
        },
    ) { padding ->
        ChatMessagingScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
            lazyListState = lazyListState,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(ChatMessagingScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ChatMessagingScreenScaffold(
                state = ChatMessagingScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenChatMessagingRoute(
                        isGroup = false,
                        title = "Mesajjj",
                        channelId = "",
                        userId = "",
                    ),
                ),
                setEvent = { },
                lazyListState = rememberLazyListState(),
                chatMessageFocusRequester = FocusRequester(),
            )
        }
    }
}
