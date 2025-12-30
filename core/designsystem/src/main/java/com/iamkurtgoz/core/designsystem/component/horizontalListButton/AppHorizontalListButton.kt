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
package com.iamkurtgoz.core.designsystem.component.horizontalListButton

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

object AppHorizontalListButton {
    @Composable
    fun <ID : Any, T : Any> Primary(
        itemList: ImmutableList<Pair<ID, T>>,
        modifier: Modifier = Modifier,
        onClick: (Pair<ID, T>) -> Unit = {},
        colors: HorizontalListButtonColors = AppHorizontalListButtonColors.primaryColors(),
        sizes: HorizontalListButtonSizes = AppHorizontalListButtonSizes.primarySizes(),
        styles: HorizontalListButtonStyles = AppHorizontalListButtonStyles.primaryStyles(),
        shapes: HorizontalListButtonShape = AppHorizontalListButtonShapes.primaryShapes(),
        horizontalScrollState: ScrollState = rememberScrollState(),
        customPadding: @Composable (Int) -> PaddingValues = { PaddingValues(all = 0.dp) },
    ) = SliderButtonImpl(
        itemList = itemList,
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        sizes = sizes,
        styles = styles,
        shapes = shapes,
        horizontalScrollState = horizontalScrollState,
        customPadding = customPadding,
    )
}

@Composable
private fun <ID : Any, T : Any> SliderButtonImpl(
    itemList: ImmutableList<Pair<ID, T>>,
    colors: HorizontalListButtonColors,
    sizes: HorizontalListButtonSizes,
    styles: HorizontalListButtonStyles,
    shapes: HorizontalListButtonShape,
    horizontalScrollState: ScrollState,
    customPadding: @Composable (Int) -> PaddingValues,
    modifier: Modifier = Modifier,
    onClick: (Pair<ID, T>) -> Unit = {},
) {
    Row(
        modifier = modifier
            .horizontalScroll(horizontalScrollState),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemList.forEachIndexed { index, item ->
            SliderButtonItemImpl(
                index = index,
                item = item,
                colors = colors,
                sizes = sizes,
                styles = styles,
                shapes = shapes,
                customPadding = customPadding,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun <ID : Any, T : Any> SliderButtonItemImpl(
    index: Int,
    item: Pair<ID, T>,
    colors: HorizontalListButtonColors,
    sizes: HorizontalListButtonSizes,
    styles: HorizontalListButtonStyles,
    shapes: HorizontalListButtonShape,
    customPadding: @Composable (Int) -> PaddingValues,
    modifier: Modifier = Modifier,
    onClick: (Pair<ID, T>) -> Unit = {},
) {
    Box(
        modifier = modifier
            .padding(customPadding.invoke(index))
            .clip(shape = shapes.roundedCornerShape)
            .background(
                color = colors.enabledContainerColor,
                shape = shapes.roundedCornerShape,
            )
            .clickable(
                onClick = {
                    onClick.invoke(item)
                },
            )
            .padding(sizes.contentPadding),
    ) {
        val title: String = when (item.second) {
            is String -> item.second as String
            is Int -> stringResource(id = item.second as Int)
            else -> "Undefined"
        }

        Text(
            text = title,
            color = colors.enabledContentColor,
            style = styles.textStyle,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    val userList = listOf("CllKrc", "MhmtKrtgz", "CelilKirca", "MehmetKurtgoz")
    val list = mutableListOf<Pair<Int, String>>()
    repeat(AppDefaults.TEN * AppDefaults.ONE) {
        list.add(Pair(it, userList.random()))
    }

    AppTheme {
        AppThemeScaffold {
            Column {
                AppHorizontalListButton.Primary(
                    itemList = list.toPersistentList(),
                )

                AppHorizontalListButton.Primary(
                    itemList = list.toPersistentList(),
                )
            }
        }
    }
}
