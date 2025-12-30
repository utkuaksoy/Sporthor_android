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
package com.iamkurtgoz.feature.home.chat.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.chip.AppChip
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.chat.ChatScreenContract
import com.iamkurtgoz.feature.home.chat.domain.types.ChatFilterType
import com.iamkurtgoz.feature.home.chat.domain.types.ChatFilterTypeList

@Composable
internal fun ChatHeader(
    state: ChatScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ChatScreenContract.Event) -> Unit,
) {
    LazyRow(
        modifier = modifier
            .fillMaxSize(),
    ) {
        itemsIndexed(
            items = ChatFilterTypeList,
            key = { index, item ->
                "$index-$item"
            },
            itemContent = { index, item ->
                AppChip.Primary(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingSmall)
                        .ifTrue(index == AppDefaults.ZERO) {
                            this.padding(start = AppTheme.spacing.spacingMedium)
                        }
                        .ifTrue(index != AppDefaults.ZERO) {
                            this.padding(start = AppTheme.spacing.spacingSmall)
                        }
                        .ifTrue(index == ChatFilterType.entries.lastIndex) {
                            this.padding(end = AppTheme.spacing.spacingMedium)
                        },
                    item = item,
                    isSelected = state.selectedFilterType == item,
                    onClick = {
                        setEvent.invoke(ChatScreenContract.Event.SetFilterType(it))
                    },
                )
            },
        )
    }
}

@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ChatHeader(
                state = ChatScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
