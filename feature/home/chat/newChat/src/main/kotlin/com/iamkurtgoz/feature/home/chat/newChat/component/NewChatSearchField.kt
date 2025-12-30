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
package com.iamkurtgoz.feature.home.chat.newChat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.chat.newChat.NewChatScreenContract

@Composable
internal fun NewChatSearchField(
    state: NewChatScreenContract.State,
    setEvent: (NewChatScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Kime:", // TODO: Localize
                style = AppTheme.typography.labelRegular,
                color = AppTheme.colors.generalColors.textSecondary,
            )

            Spacer(
                modifier = Modifier
                    .width(AppTheme.dimens.dp6),
            )

            BasicTextField(
                value = state.textSearch.value,
                onValueChange = {
                    setEvent(NewChatScreenContract.Event.SetSearch(it))
                },
                modifier = Modifier
                    .height(AppTheme.dimens.dp48)
                    .fillMaxWidth(),
                singleLine = true,
                textStyle = AppTheme.typography.labelRegular.copy(
                    color = AppTheme.colors.generalColors.textPrimary,
                ),
                maxLines = AppDefaults.LINE_LIMIT_SINGLE,
                decorationBox = { basicTextField ->
                    Box(
                        modifier = Modifier
                            .height(AppTheme.dimens.dp48)
                            .weight(AppDefaults.WEIGHT_FULL),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (state.textSearch.value.isEmpty()) {
                            Text(
                                text = "Ara", // TODO: Localize
                                style = AppTheme.typography.labelRegular,
                                color = AppTheme.colors.generalColors.textSecondary,
                            )
                        }
                        basicTextField()
                    }
                },
            )
        }

        HorizontalDivider()
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            NewChatSearchField(
                state = NewChatScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
