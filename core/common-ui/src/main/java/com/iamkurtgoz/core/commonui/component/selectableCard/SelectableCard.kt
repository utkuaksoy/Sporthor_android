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
package com.iamkurtgoz.core.commonui.component.selectableCard

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
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
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteGridList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.base.Listable
import kotlinx.collections.immutable.persistentListOf
import java.util.UUID

object SelectableCard {
    @Composable
    fun Primary(
        index: Int,
        isSelected: Boolean,
        imageData: Any?,
        text: String,
        onSelect: (Int) -> Unit,
        modifier: Modifier = Modifier,
        colors: SelectableCardColor = SelectableCardColors.primaryColors(),
        sizes: SelectableCardSize = SelectableCardSizes.primarySizes(),
        borders: SelectableCardBorder = SelectableCardBorders.primaryBorders(),
        shapes: SelectableCardShape = SelectableCardShapes.primaryShapes(),
        styles: SelectableCardStyle = SelectableCardStyles.primaryStyles(),
    ) = SelectableCardImpl(
        index = index,
        isSelected = isSelected,
        imageData = imageData,
        text = text,
        onSelect = onSelect,
        modifier = modifier,
        colors = colors,
        sizes = sizes,
        borders = borders,
        shapes = shapes,
        styles = styles,
    )
}

@Composable
private fun SelectableCardImpl(
    index: Int,
    isSelected: Boolean,
    imageData: Any?,
    text: String,
    onSelect: (Int) -> Unit,
    colors: SelectableCardColor,
    sizes: SelectableCardSize,
    borders: SelectableCardBorder,
    shapes: SelectableCardShape,
    styles: SelectableCardStyle,
    modifier: Modifier = Modifier,
) {
    val containerColor by colors.containerColor(isSelected = isSelected)
    val contentColor by colors.contentColor(isSelected = isSelected)
    val iconBackgroundColor by colors.iconBackgroundColor(isSelected = isSelected)
    val border by borders.border(isSelected = isSelected)
    val containerShape by shapes.containerShape(isSelected = isSelected)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(sizes.cardRatio)
            .clip(containerShape)
            .clickable {
                onSelect.invoke(index)
            },
        shape = containerShape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = AppTheme.dimens.dp4,
        border = border,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            imageData?.let {
                AppAsyncImageLoader.Load(
                    data = imageData,
                    contentDescription = "image",
                    modifier = Modifier
                        .background(
                            color = iconBackgroundColor,
                            shape = shapes.iconBackgroundShape,
                        )
                        .clip(
                            shape = shapes.iconBackgroundShape,
                        )
                        .size(sizes.iconSize),
                )
            }

            Text(
                text = text,
                style = styles.textStyle,
            )
        }
    }
}

@Keep
private data class PreviewItem(
    override val uuid: String?,
    val text: String,
) : Listable

@OptIn(ExperimentalMaterial3Api::class)
@PreviewAppWithNightMode
@Composable
private fun Preview() {
    var selectedIndex by remember { mutableIntStateOf(AppDefaults.ZERO) }
    AppTheme {
        AppThemeScaffold {
            InfiniteGridList(
                itemList = persistentListOf(
                    PreviewItem(
                        uuid = UUID.randomUUID().toString(),
                        text = "Voleybol",
                    ),
                    PreviewItem(
                        uuid = UUID.randomUUID().toString(),
                        text = "Basketbol",
                    ),
                    PreviewItem(
                        uuid = UUID.randomUUID().toString(),
                        text = "Futbol",
                    ),
                ),
                columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_TWO),
                contentPadding = PaddingValues(
                    bottom = AppTheme.spacing.spacingMedium,
                ),
                rowContent = { index, item ->
                    SelectableCard.Primary(
                        index = index,
                        isSelected = selectedIndex == index,
                        imageData = resourcesR.drawable.img_user_role_athlete,
                        text = item.text,
                        modifier = Modifier
                            .padding(top = AppTheme.spacing.spacingMedium)
                            .padding(start = if (index % 2 == AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall)
                            .padding(end = if (index % 2 != AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall),
                        onSelect = {
                            selectedIndex = it
                        },
                    )
                },
            )
        }
    }
}
