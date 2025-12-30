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
package com.iamkurtgoz.feature.home.editTrainingGroup.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.feature.home.editTrainingGroup.domain.model.GetSeasonsUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSeasonDialog(
    showSheet: Boolean,
    seasons: List<GetSeasonsUIModel>,
    onDismissRequest: () -> Unit,
    onSeasonSelected: (GetSeasonsUIModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { true },
        )

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = AppTheme.colors.generalColors.backgroundPrimary,
            scrimColor = AppTheme.colors.generalColors.backgroundPrimary.copy(
                alpha = AppDefaults.COMPOSE_COLORS_THREE_QUARTER_ALPHA,
            ),
            modifier = modifier,
        ) {
            SelectSeasonDialogContent(
                seasons = seasons,
                onSeasonSelected = onSeasonSelected,
            )
        }
    }
}

@Composable
private fun SelectSeasonDialogContent(
    seasons: List<GetSeasonsUIModel>,
    onSeasonSelected: (GetSeasonsUIModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.spacing.spacingMedium,
                vertical = AppTheme.spacing.spacingMedium,
            ),
    ) {
        Text(
            text = "Sezon Seçimi", // TODO: Localize
            style = AppTheme.typography.heading04,
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingSmall))

        seasons.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeasonSelected(item) }
                    .padding(vertical = AppTheme.spacing.spacingSmall),
            ) {
                Text(
                    text = item.name ?: "-",
                    style = AppTheme.typography.bodyLarge,
                )
            }

            if (index < seasons.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}
