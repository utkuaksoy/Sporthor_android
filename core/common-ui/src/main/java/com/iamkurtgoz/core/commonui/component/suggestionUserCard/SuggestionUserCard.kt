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

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.base.Listable
import kotlinx.collections.immutable.persistentListOf

object SuggestionUserCard {
    @Composable
    fun Primary(
        index: Int,
        isFollowing: Boolean,
        imageData: Any?,
        badgeData: Any?,
        containerText: String,
        onButtonClick: (Int) -> Unit,
        onClickAction: (Int) -> Unit,
        modifier: Modifier = Modifier,
        colors: SuggestionUserCardColor = SuggestionUserCardColors.primaryColors(),
        sizes: SuggestionUserCardSize = SuggestionUserCardSizes.primarySizes(),
        borders: SuggestionUserCardBorder = SuggestionUserCardBorders.primaryBorders(),
        shapes: SuggestionUserCardShape = SuggestionUserCardShapes.primaryShapes(),
        styles: SuggestionUserCardStyle = SuggestionUserCardStyles.primaryStyles(),
    ) = SuggestionUserCardImpl(
        index = index,
        isFollowing = isFollowing,
        imageData = imageData,
        badgeData = badgeData,
        containerText = containerText,
        onButtonClick = onButtonClick,
        onClickAction = onClickAction,
        modifier = modifier,
        colors = colors,
        sizes = sizes,
        borders = borders,
        shapes = shapes,
        styles = styles,
    )
}

@Composable
private fun SuggestionUserCardImpl(
    index: Int,
    isFollowing: Boolean,
    imageData: Any?,
    badgeData: Any?,
    containerText: String,
    onButtonClick: (Int) -> Unit,
    onClickAction: (Int) -> Unit,
    colors: SuggestionUserCardColor,
    sizes: SuggestionUserCardSize,
    borders: SuggestionUserCardBorder,
    shapes: SuggestionUserCardShape,
    styles: SuggestionUserCardStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(sizes.width)
            .aspectRatio(sizes.cardRatio)
            .background(
                color = colors.suggestionUserCardContainerColor,
                shape = shapes.suggestionUserCardContainerShape,
            )
            .border(border = borders.borderStroke, shape = shapes.suggestionUserCardContainerShape)
            .clip(shape = shapes.suggestionUserCardContainerShape)
            .clickable {
                onClickAction.invoke(index)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        UserImageView(
            data = imageData,
            badgeData = badgeData,
            size = sizes.imageSize,
        )

        Text(
            text = containerText,
            style = styles.textStyle,
            modifier = Modifier.padding(
                top = sizes.textTopPadding,
            ),
        )

        if (isFollowing) {
            AppButton.OutlineSmall(
                text = "Takip Ediliyor", // TODO: Localize
                onClick = {
                    onButtonClick(index)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sizes.buttonHorizontalPadding)
                    .padding(top = sizes.buttonTopPadding),
            )
        } else {
            AppButton.SecondarySmall(
                text = "Takip Et", // TODO: Localize
                onClick = {
                    onButtonClick(index)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sizes.buttonHorizontalPadding)
                    .padding(top = sizes.buttonTopPadding),
            )
        }
    }
}

@Keep
private data class MockProfile(
    override val uuid: String?,
    val index: Int,
    val name: String,
    val isFollowing: Boolean,
    val imageResId: Int,
    val badgeResId: Int?,
    val buttonText: String,
) : Listable

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    val itemList = persistentListOf(
        MockProfile(
            uuid = "1",
            index = 0,
            name = "Dilara Çevik",
            isFollowing = false,
            imageResId = android.R.drawable.sym_def_app_icon,
            badgeResId = android.R.drawable.star_on,
            buttonText = "Takip Et",
        ),
        MockProfile(
            uuid = "2",
            index = 1,
            name = "Aslı Yıldız",
            isFollowing = true,
            imageResId = android.R.drawable.sym_def_app_icon,
            badgeResId = null,
            buttonText = "Takip Ediliyor",
        ),
        MockProfile(
            uuid = "3",
            index = 2,
            name = "Charlie",
            isFollowing = false,
            imageResId = android.R.drawable.sym_def_app_icon,
            badgeResId = null,
            buttonText = "Takip Et",
        ),
    )
    var selectedIndex by remember { mutableIntStateOf(AppDefaults.ZERO) }
    AppTheme {
        AppThemeScaffold {
            LazyRow {
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
                                .padding(start = if (index % 2 == AppDefaults.ZERO) AppTheme.spacing.spacingSmall else AppTheme.spacing.spacingSmall)
                                .padding(end = if (index % 2 != AppDefaults.ZERO) AppTheme.spacing.spacingSmall else AppTheme.spacing.spacingSmall),
                            onClickAction = {
                                selectedIndex = it
                            },
                            isFollowing = item.isFollowing,
                            imageData = resourcesR.drawable.img_user_role_club_official,
                            badgeData = resourcesR.drawable.img_user_role_club_official,
                            containerText = item.name,
                            onButtonClick = {
                                selectedIndex = it
                            },
                        )
                    },
                )
            }
        }
    }
}
