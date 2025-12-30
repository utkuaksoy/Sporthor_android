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
package com.iamkurtgoz.core.commonui.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteGridList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.domain.model.base.Listable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Listable> ShareUIUserBranchPage(
    itemList: ImmutableList<T>,
    modifier: Modifier = Modifier,
    title: String? = null,
    subTitle: String? = null,
    rowContent: @Composable (Int, T) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        title?.let {
            Text(
                text = title,
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingLarge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        subTitle?.let {
            Text(
                text = subTitle,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textDisabled,
            )
        }

        InfiniteGridList(
            itemList = itemList,
            columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_TWO),
            contentPadding = PaddingValues(
                vertical = AppTheme.spacing.spacingLarge,
            ),
            rowContent = rowContent,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ShareUIUserBranchPage(
                title = "Hangi sporla ilgileniyorsun?",
                subTitle = "* En az birini seçerek devam edebilirsin",
                itemList = persistentListOf(),
                rowContent = { _, _ -> },
            )
        }
    }
}
