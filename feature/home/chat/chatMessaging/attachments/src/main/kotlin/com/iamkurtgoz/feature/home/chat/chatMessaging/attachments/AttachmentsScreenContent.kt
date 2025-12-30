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
package com.iamkurtgoz.feature.home.chat.chatMessaging.attachments

import android.util.Base64
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenChatMessagingDetailAttachmentsRoute
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.component.AttachmentsTopRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.component.DocumentAttachmentsRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.component.DocumentFile
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.component.MediaAttachmentsRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.component.MediaAttachmentsRowItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AttachmentsScreenContent(
    state: AttachmentsScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (AttachmentsScreenContract.Event) -> Unit,
) {
    var selected by remember { mutableStateOf("Medya") }

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        AttachmentsTopRow(
            items = listOf("Medya", "Belgeler"),
            selectedItem = selected,
            onItemSelected = { selected = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp),
            color = Color(0xFFF4F4F4),
        )

        when (selected) {
            "Medya" -> {
                MediaAttachmentsRow(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    imageList = state.mediaList?.mapIndexed { index, media ->
                        MediaAttachmentsRowItem(
                            data = Base64.decode(media.content, Base64.DEFAULT),
                            uuid = index.toString(),
                        )
                    }?.toImmutableList() ?: persistentListOf(),
                )
            }

            "Belgeler" -> {
                val documentList = state.documentList?.map { document ->
                    DocumentFile(fileName = document.from?.name)
                }

                DocumentAttachmentsRow(documents = documentList)
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AttachmentsScreenContent(
                state = AttachmentsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenChatMessagingDetailAttachmentsRoute(
                        userId = "",
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
