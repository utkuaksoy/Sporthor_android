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
package com.iamkurtgoz.feature.home.editTrainingGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.SelectedClubSection
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenEditTrainingGroupScreenRoute
import com.iamkurtgoz.core.navigation.model.home.editTrainingGroup.HomeScreenEditTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun EditTrainingGroupScreenContent(
    state: EditTrainingGroupScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (EditTrainingGroupScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            AppCircleButton.SecondaryGrayLarge(
                icon = resourcesR.drawable.img_back_arrow,
                onClick = {
                    setEvent.invoke(EditTrainingGroupScreenContract.Event.NavigateUp)
                },
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Antrenman grubunu güncelle.", // TODO: Localize
                style = AppTheme.typography.heading04,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingRegular)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Antrenman grubunu güncelle ve devam et.", // TODO: Localize
                style = AppTheme.typography.subtitleLarge.copy(
                    fontWeight = FontWeight.Normal,
                ),
                color = AppTheme.colors.generalColors.textSecondary,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            SelectedClubSection(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                clubName = state.route.model.teamName ?: "-",
                clubLogo = state.route.model.teamLogo ?: "",
                onChangeClick = {
                    setEvent.invoke(EditTrainingGroupScreenContract.Event.NavigateUp)
                },
                showChangeButton = true,
            )
        }

        item {
            BasicText(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.spacing.spacingSmall),
                text = "Sezon Seçimi", // TODO: Localize
                style = AppTheme.typography.labelMedium,
                maxLines = AppDefaults.LINE_LIMIT_SINGLE,
                overflow = TextOverflow.Ellipsis,
            )
        }

        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .height(AppTheme.dimens.dp48)
                    .background(
                        color = AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledContainerColor,
                        shape = AppTheme.shapes.radiusMedium,
                    )
                    .padding(
                        PaddingValues(
                            vertical = AppTheme.dimens.dp12,
                            horizontal = AppTheme.dimens.dp16,
                        ),
                    )
                    .clickable {
                        setEvent.invoke(EditTrainingGroupScreenContract.Event.ShowSelectSeasonDialog)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                ) {
                    Text(
                        text = state.selectedSeason?.name ?: "Sezon seçiniz", // TODO: Localize
                        style = AppTheme.typography.bodyLargeCompact,
                        color = if (state.selectedSeason == null) {
                            AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryPlaceholderColor
                        } else {
                            AppTheme.colors.textFieldColors.textFieldPrimaryColors.textFieldPrimaryEnabledContentColor
                        },
                    )
                }

                Spacer(
                    modifier = Modifier
                        .width(AppTheme.dimens.dp8),
                )

                IconButton(
                    modifier = Modifier
                        .size(AppTheme.dimens.dp18),
                    onClick = {
                    },
                    content = {
                        Image(
                            modifier = Modifier
                                .size(AppTheme.dimens.dp18),
                            painter = painterResource(id = resourcesR.drawable.img_arrow_down),
                            contentDescription = "trailing icon",
                        )
                    },
                )
            }
        }

        item {
            AppTextField.Primary(
                title = "Grup Adı", // TODO: Localize
                placeholder = "Grup Adı", // TODO: Localize
                value = state.textGroupName.value,
                onValueChange = {
                    setEvent.invoke(EditTrainingGroupScreenContract.Event.SetGroupName(it))
                },
                suggestions = state.suggestions,
                isError = state.textGroupName.isError && state.isFieldErrorShow,
                hint = "Lütfen geçerli bir grup adını girin", // TODO: Localize
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            EditTrainingGroupScreenContent(
                state = EditTrainingGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenEditTrainingGroupScreenRoute(
                        model = HomeScreenEditTrainingGroupScreenNavigationModel(
                            teamId = null,
                            teamName = null,
                            teamLogo = null,
                            season = null,
                            id = null,
                            groupName = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
