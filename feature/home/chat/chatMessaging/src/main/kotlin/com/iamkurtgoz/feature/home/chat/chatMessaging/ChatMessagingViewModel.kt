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

import android.util.Base64
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.qualifiers.IoDispatcher
import com.iamkurtgoz.core.common.qualifiers.MainDispatcher
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.controller.SignalRController
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.eventbus.impl.ChatMessagingEventBus
import com.iamkurtgoz.domain.extensions.toAlertDialog
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.OrderByParams
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemFromUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.ChatMessageItemUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.model.withDateHeaders
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.useCase.GetMessagesUseCase
import com.iamkurtgoz.feature.home.chat.chatMessaging.domain.useCase.GetMessagesUseCaseParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
internal class ChatMessagingViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    savedStateHandle: SavedStateHandle,
    private val appPreferences: AppPreferences,
    private val signalRController: SignalRController,
    private val getMessagesUseCase: GetMessagesUseCase,
) : CoreViewModel<ChatMessagingScreenContract.State, ChatMessagingScreenContract.SideEffect, ChatMessagingScreenContract.Event>(
    initialState = ChatMessagingScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        route = savedStateHandle.toRoute(),

    ),
) {
    private data class PendingOutgoingMessage(
        val content: String,
        val timestampMillis: Long,
    )

    private val pendingOutgoingMessages = mutableListOf<PendingOutgoingMessage>()
    private var currentUserId: String? = null

    override fun setEvent(event: ChatMessagingScreenContract.Event) {
        when (event) {
            is ChatMessagingScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is ChatMessagingScreenContract.Event.NavigateUp -> setSideEffect(ChatMessagingScreenContract.SideEffect.NavigateUp)
            is ChatMessagingScreenContract.Event.PopBackStack -> setSideEffect(ChatMessagingScreenContract.SideEffect.PopBackStack)
            is ChatMessagingScreenContract.Event.DismissDialogs -> dismissDialogs()
            is ChatMessagingScreenContract.Event.SetTextMessage -> setTextMessage(event.text)
            is ChatMessagingScreenContract.Event.SendMessage -> sendMessage()
            is ChatMessagingScreenContract.Event.UpdateEventBusStatus -> updateEventBusStatus(event.eventBusState)
            is ChatMessagingScreenContract.Event.SetKeyboardShow -> setKeyboardShow(event.isKeyboardShow)
            is ChatMessagingScreenContract.Event.FetchChatMessagingList -> fetchChatMessagingList(event.fetchParam)
            is ChatMessagingScreenContract.Event.SetShowFileSelectDialogState -> setShowFileSelectDialogState(event.isShowFileSelectDialog)
            is ChatMessagingScreenContract.Event.SendSelectedFile -> sendSelectedFile(event.path, event.extension, event.type)
            is ChatMessagingScreenContract.Event.NavigateToMediaViewer -> setSideEffect(ChatMessagingScreenContract.SideEffect.NavigateToMediaViewer(routeType = event.routeType))
            is ChatMessagingScreenContract.Event.NavigateToChatMessagingDetailUser -> setSideEffect(ChatMessagingScreenContract.SideEffect.NavigateToChatMessagingDetailUser(userId = event.userId, groupId = event.groupId))
            is ChatMessagingScreenContract.Event.NavigateToChatMessagingDetailGroup -> setSideEffect(ChatMessagingScreenContract.SideEffect.NavigateToChatMessagingDetailGroup(userId = event.userId, groupId = event.groupId))
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch {
        listenMessagesListChange()
        appPreferences.currentPreferenceState
            .onEach { preferences ->
                currentUserId = preferences.userId
            }
            .launchIn(viewModelScope)
        fetchChatMessagingList(fetchParam = FetchParam.INITIAL)
        signalRController.joinGroup(viewState.route.channelId)
    }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun listenMessagesListChange() {
        state.distinctUntilChangedBy { it.messages }
            .onEach {
                updateState { state ->
                    state.copy(
                        groupedMessages = state.messages.withDateHeaders().toPersistentList(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setTextMessage(text: String) {
        val textFieldValue = viewState.textMessage.copy(
            value = text,
            isError = false,
        )
        updateState { state ->
            state.copy(
                textMessage = textFieldValue,
            )
        }
        sendNotifyTypingDelay()
    }

    private fun sendMessage() = viewModelScope.launch {
        val textMessage = viewState.textMessage.value
        if (textMessage.isBlank()) {
            return@launch
        }
        Timber.d(
            "Chat send: userId=%s routeUserId=%s channelId=%s message=%s",
            currentUserId,
            viewState.route.userId,
            viewState.route.channelId,
            textMessage,
        )
        setTextMessage("")
        if (!currentUserId.isNullOrBlank()) {
            val currentUser = viewState.users.firstOrNull { it.id == currentUserId }
            val newItem = ChatMessageItemUIModel(
                content = textMessage,
                fileExtension = null,
                from = ChatMessageItemFromUIModel(
                    id = currentUserId,
                    image = currentUser?.imageUrl,
                    name = currentUser?.name,
                ),
                id = UUID.randomUUID().toString(),
                messageType = SignalRMessageType.TEXT,
                sendDate = LocalDateTime.now(),
                shortDate = null,
            )
            pendingOutgoingMessages.add(
                PendingOutgoingMessage(
                    content = textMessage,
                    timestampMillis = System.currentTimeMillis(),
                ),
            )
            val messages = viewState.messages.toMutableList().apply {
                add(index = AppDefaults.ZERO, element = newItem)
            }
            updateState { state ->
                state.copy(
                    messages = messages.toPersistentList(),
                )
            }
            setSideEffect(ChatMessagingScreenContract.SideEffect.ScrollToBottom)
        }
        signalRController.sendMessage(
            group = viewState.route.channelId,
            message = textMessage,
            type = SignalRMessageType.TEXT,
        )
    }

    private fun fetchChatMessagingList(fetchParam: FetchParam) {
        if (viewState.paginationInitialing || viewState.paginationLoading || viewState.paginationReloading) {
            return
        }

        when (fetchParam) {
            FetchParam.INITIAL -> {
                updateState { state ->
                    state.copy(
                        paginationPage = AppDefaults.LIST_PARAM_PAGE,
                        paginationInitialing = true,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = true,
                        messages = persistentListOf(),
                    )
                }
            }
            FetchParam.RELOAD -> {
                updateState { state ->
                    state.copy(
                        paginationPage = AppDefaults.LIST_PARAM_PAGE,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = true,
                        paginationHasNext = true,
                        messages = persistentListOf(),
                    )
                }
            }
            FetchParam.NEXT_PAGE -> {
                if (viewState.paginationHasNext) {
                    updateState { state ->
                        state.copy(
                            paginationPage = viewState.paginationPage.plus(AppDefaults.ONE),
                            paginationInitialing = false,
                            paginationLoading = true,
                            paginationReloading = false,
                        )
                    }
                }
            }
        }

        if (!viewState.paginationHasNext) {
            return
        }

        val params = GetMessagesUseCaseParams(
            page = viewState.paginationPage,
            channelId = viewState.route.channelId,
            orderBy = OrderByParams.DESC,
        )

        getMessagesUseCase.invoke(params)
            .requester
            .onLoading {
                updateState { state ->
                    state.copy(
                        isShimmerLoading = fetchParam == FetchParam.INITIAL || fetchParam == FetchParam.RELOAD,
                        paginationInitialing = fetchParam == FetchParam.INITIAL,
                        paginationLoading = fetchParam == FetchParam.NEXT_PAGE,
                        paginationReloading = fetchParam == FetchParam.RELOAD,
                    )
                }
            }
            .onError { error ->
                updateState {
                    it.copy(
                        isShimmerLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        alertDialogModel = error.toAlertDialog,
                    )
                }
            }
            .callWithSuccess { response ->
                val responseMessages = response.messages?.filterNotNull() ?: emptyList()
                val currentList: List<ChatMessageItemUIModel> = when (fetchParam) {
                    FetchParam.INITIAL -> responseMessages
                    FetchParam.RELOAD -> responseMessages
                    FetchParam.NEXT_PAGE -> {
                        val list = viewState.messages.toMutableList()
                        list.apply {
                            addAll(responseMessages)
                        }
                    }
                }
                val paginationHasNext = responseMessages.isNotEmpty()

                updateState {
                    it.copy(
                        isShimmerLoading = false,
                        paginationInitialing = false,
                        paginationLoading = false,
                        paginationReloading = false,
                        paginationHasNext = paginationHasNext,
                        messages = currentList.toPersistentList(),
                        users = response.users?.filterNotNull()?.toPersistentList() ?: persistentListOf(),
                    )
                }
            }
    }

    private fun updateEventBusStatus(eventBusState: ChatMessagingEventBus.Event) {
        when (eventBusState) {
            is ChatMessagingEventBus.Event.ReceiveNewMessage -> {
                Timber.d(
                    "Chat receive: messageUserId=%s currentUserId=%s routeUserId=%s channelId=%s message=%s",
                    eventBusState.messageUserId,
                    currentUserId,
                    viewState.route.userId,
                    viewState.route.channelId,
                    eventBusState.messageContent,
                )
                val isFromCurrentUser = !currentUserId.isNullOrBlank() && eventBusState.messageUserId == currentUserId
                if (isFromCurrentUser && !eventBusState.messageContent.isNullOrBlank()) {
                    val nowMillis = System.currentTimeMillis()
                    val matchIndex = pendingOutgoingMessages.indexOfFirst { pending ->
                        pending.content == eventBusState.messageContent &&
                            nowMillis - pending.timestampMillis <= 10_000L
                    }
                    if (matchIndex != -1) {
                        pendingOutgoingMessages.removeAt(matchIndex)
                        return
                    }
                }
                val user = viewState.users.firstOrNull { it.id == eventBusState.messageUserId }
                val messages = viewState.messages.toMutableList()
                val item = ChatMessageItemUIModel(
                    content = eventBusState.messageContent,
                    fileExtension = eventBusState.attachment,
                    from = ChatMessageItemFromUIModel(
                        id = eventBusState.messageUserId,
                        image = user?.imageUrl,
                        name = user?.name,
                    ),
                    id = UUID.randomUUID().toString(),
                    messageType = eventBusState.type,
                    sendDate = LocalDateTime.now(),
                    shortDate = null,
                )
                messages.add(
                    index = AppDefaults.ZERO,
                    element = item,
                )
                updateState { state ->
                    state.copy(
                        messages = messages.toPersistentList(),
                    )
                }
                setSideEffect(ChatMessagingScreenContract.SideEffect.ScrollToBottom)
            }
            is ChatMessagingEventBus.Event.ReceiveUserTyping -> {
                userTypingDelay()
            }
        }
    }

    // Notify Typing
    private var sendNotifyTypingJob: Job? = null
    private fun sendNotifyTypingDelay() {
        if (sendNotifyTypingJob == null) {
            sendNotifyTypingJob = viewModelScope.launch {
                signalRController.notifyTyping(viewState.route.channelId)
                delay(ChatMessagingScreenContract.Static.NOTIFY_TYPING_DELAY)
                sendNotifyTypingJob = null
            }
        }
    }

    // User Typing
    private var userTypingJob: Job? = null
    private fun userTypingDelay() {
        if (userTypingJob == null) {
            userTypingJob = viewModelScope.launch {
                updateState { state ->
                    state.copy(
                        isTyping = true,
                    )
                }
                delay(ChatMessagingScreenContract.Static.USER_TYPING_DELAY)
                updateState { state ->
                    state.copy(
                        isTyping = false,
                    )
                }
                userTypingJob = null
            }
        }
    }

    private fun setKeyboardShow(isKeyboardShow: Boolean) {
        updateState { state ->
            state.copy(
                isKeyboardShow = isKeyboardShow,
            )
        }
    }

    private fun setShowFileSelectDialogState(isShow: Boolean) {
        updateState { state ->
            state.copy(
                isShowFileSelectDialog = isShow,
            )
        }

        if (isShow) {
            setSideEffect(ChatMessagingScreenContract.SideEffect.HideKeyboard)
        } else {
            setSideEffect(ChatMessagingScreenContract.SideEffect.ShowKeyboard)
        }
    }

    private fun sendSelectedFile(path: String?, extension: String?, type: SignalRMessageType?) = viewModelScope.launch(ioDispatcher) {
        withContext(mainDispatcher) {
            updateState { state ->
                state.copy(
                    isLoading = true,
                )
            }
        }

        delay(ChatMessagingScreenContract.Static.SEND_FILE_DELAY)

        if (path == null || extension == null || type == null) {
            return@launch
        }

        val file = File(path)
        if (!file.isFile && !file.exists()) {
            return@launch
        }

        try {
            val bytes = File(path).readBytes()
            val fileBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

            signalRController.sendMessage(
                group = viewState.route.channelId,
                message = fileBase64,
                type = type,
                extension = extension,
            )
            withContext(mainDispatcher) {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(mainDispatcher) {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        runBlocking {
            signalRController.leaveGroup(viewState.route.channelId)
        }
    }
}
