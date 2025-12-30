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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.UpdateGroupScreenRoute
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.component.UpdateGroupBodyRow
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.component.UpdateGroupTopRow
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UpdateGroupScreenContent(
    state: UpdateGroupScreenContract.State,
    setEvent: (UpdateGroupScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            UpdateGroupTopRow(
                modifier = Modifier,
                groupName = state.groupName,
                groupImage = state.groupImage,
            )
        }

        item {
            UpdateGroupBodyRow(
                icons = state.iconList ?: persistentListOf(),
                onIconClick = { index, selectedIcon ->
                    if (index == 0) {
                        setEvent.invoke(UpdateGroupScreenContract.Event.SetShowStatePhotoPicker(true))
                    } else {
                        setEvent(UpdateGroupScreenContract.Event.IconSelected(selectedIcon))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            UpdateGroupScreenContent(
                state = UpdateGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = UpdateGroupScreenRoute(
                        groupId = "",
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
