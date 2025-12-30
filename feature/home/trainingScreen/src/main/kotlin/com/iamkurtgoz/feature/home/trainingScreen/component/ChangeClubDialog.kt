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
package com.iamkurtgoz.feature.home.trainingScreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeClubDialog(
    showSheet: Boolean,
    clubs: List<HomeScreenTrainingScreenNavigateModel>,
    onDismissRequest: () -> Unit,
    onClubSelected: (HomeScreenTrainingScreenNavigateModel) -> Unit,
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
            ChangeClubDialogContent(
                clubs = clubs,
                onClubSelected = onClubSelected,
            )
        }
    }
}

@Composable
private fun ChangeClubDialogContent(
    clubs: List<HomeScreenTrainingScreenNavigateModel>,
    onClubSelected: (HomeScreenTrainingScreenNavigateModel) -> Unit,
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
            text = "Kulüp Değiştir",
            style = AppTheme.typography.heading04,
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingSmall))

        clubs.forEachIndexed { index, club ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClubSelected(club) }
                    .padding(vertical = AppTheme.spacing.spacingSmall),
            ) {
                AsyncImage(
                    model = club.logo,
                    contentDescription = "${club.clubName} logo",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(AppTheme.spacing.spacingMedium))

                Text(
                    text = club.clubName ?: "-",
                    style = AppTheme.typography.bodyLarge,
                )
            }

            if (index < clubs.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun ChangeClubDialogPreview() {
    val sampleClubs = listOf(
        HomeScreenTrainingScreenNavigateModel(
            clubId = "MQ==", // Base64 örnek
            founderUserId = "Mg==",
            clubName = "Galatasaray",
            logo = "https://picsum.photos/seed/gs/48",
        ),
        HomeScreenTrainingScreenNavigateModel(
            clubId = "Mw==",
            founderUserId = "NA==",
            clubName = "Real Madrid",
            logo = "https://picsum.photos/seed/rm/48",
        ),
    )

    AppTheme {
        AppThemeSurface {
            Box(Modifier.fillMaxSize()) {
                ChangeClubDialogContent(
                    clubs = sampleClubs,
                    onClubSelected = { },
                )
            }
        }
    }
}
