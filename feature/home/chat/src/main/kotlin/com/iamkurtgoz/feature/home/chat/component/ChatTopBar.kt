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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.chat.ChatScreenContract

@Composable
internal fun ChatTopBar(
    state: ChatScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ChatScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = AppTheme.spacing.spacingSmall)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Mesajlar", // TODO: Localize
                style = AppTheme.typography.heading05,
            )

            AppCircleButton.SecondarySmall(
                icon = resourcesR.drawable.img_plus,
                onClick = {
                    setEvent.invoke(ChatScreenContract.Event.NavigateToNewChatScreen)
                },
            )
        }

        AppTextField.SearchField(
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(top = AppTheme.spacing.spacingSmall),
            value = state.textSearch.value,
            placeholder = "Mesajlarda ara", // TODO: Localize
            onValueChange = {
                setEvent.invoke(ChatScreenContract.Event.SetSearch(it))
            },
            leadingIcon = resourcesR.drawable.img_search,
        )

        Spacer(
            modifier = Modifier
                .height(AppTheme.dimens.dp16),
        )

        HorizontalDivider(
            color = AppTheme.colors.generalColors.borderSoft200,
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ChatTopBar(
                state = ChatScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                modifier = Modifier,
                setEvent = {},
            )
        }
    }
}
