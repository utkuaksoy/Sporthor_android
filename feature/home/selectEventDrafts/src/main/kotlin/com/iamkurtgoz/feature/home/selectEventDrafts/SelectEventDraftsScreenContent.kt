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
package com.iamkurtgoz.feature.home.selectEventDrafts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.selectEventDrafts.component.SelectEventDraftsScreenRow

@Composable
internal fun SelectEventDraftsScreenContent(
    state: SelectEventDraftsScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SelectEventDraftsScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = AppTheme.spacing.spacingHuge,
        ),
    ) {
        itemsIndexed(
            items = state.calendarDetailEventUIModel?.tasks?.filterNotNull() ?: listOf(),
            key = { index, item ->
                "$index${item.id}"
            },
            itemContent = { index, item ->
                SelectEventDraftsScreenRow(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                    item = item,
                    onItemClick = {
                        setEvent.invoke(SelectEventDraftsScreenContract.Event.NavigateToAddEvent(it))
                    },
                )
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectEventDraftsScreenContent(
                state = SelectEventDraftsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
