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
package com.iamkurtgoz.core.commonui.component.suggestionUserCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.iamkurtgoz.core.common.extensions.isFirstIndex
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.domain.model.view.BasicSuggestionUserCardModel
import com.iamkurtgoz.fake.model.view.MockBasicSuggestionUserCardModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun BasicSuggestionUserCard(
    itemList: ImmutableList<BasicSuggestionUserCardModel>,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = AppTheme.typography.subtitleLarge.copy(
        lineHeight = AppTheme.dimens.sp20,
    ),
    onClickCardAction: (BasicSuggestionUserCardModel) -> Unit = {},
    onClickButtonAction: (BasicSuggestionUserCardModel) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "Tanıyor olabileceğin kişiler", // TODO: Localize
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            style = titleTextStyle,
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = AppTheme.spacing.spacingMedium,
            ),
            content = {
                itemsIndexed(
                    items = itemList,
                    key = { index, item ->
                        "${item.uuid}-$index"
                    },
                    itemContent = { index, item ->
                        SuggestionUserCard.Primary(
                            index = index,
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingMedium)
                                .ifTrue(!index.isFirstIndex()) {
                                    this.padding(start = AppTheme.spacing.spacingSmall)
                                },
                            onClickAction = {
                                onClickCardAction.invoke(item)
                            },
                            isFollowing = item.isFollowing,
                            imageData = item.imageData,
                            badgeData = item.badgeData,
                            containerText = item.name,
                            onButtonClick = {
                                onClickButtonAction.invoke(item)
                            },
                        )
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
        AppThemeScaffold {
            BasicSuggestionUserCard(
                modifier = Modifier
                    .background(AppTheme.colors.generalColors.backgroundWeak100)
                    .padding(top = AppTheme.spacing.spacingLarge)
                    .padding(bottom = AppTheme.spacing.spacingLarge),
                itemList = MockBasicSuggestionUserCardModel.itemList.toPersistentList(),
                onClickCardAction = {
                },
                onClickButtonAction = {
                },
            )
        }
    }
}
