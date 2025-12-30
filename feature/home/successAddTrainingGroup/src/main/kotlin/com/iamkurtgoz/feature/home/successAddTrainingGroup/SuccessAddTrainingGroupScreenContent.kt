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
package com.iamkurtgoz.feature.home.successAddTrainingGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSuccessAddTrainingGroupRoute
import com.iamkurtgoz.core.navigation.model.home.successAddTrainingGroup.HomeScreenSuccessAddTrainingGroupScreenNavigationModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SuccessAddTrainingGroupScreenContent(
    state: SuccessAddTrainingGroupScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SuccessAddTrainingGroupScreenContract.Event) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppTheme.colors.generalColors.green100)
                    .padding(bottom = AppTheme.dimens.dp72),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp72)
                        .align(Alignment.BottomCenter),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(AppTheme.shapes.radiusCircle)
                            .size(AppTheme.dimens.dp72)
                            .border(
                                width = AppTheme.dimens.dp2,
                                color = AppTheme.colors.generalColors.foregroundDisabled,
                                shape = AppTheme.shapes.radiusCircle,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        AppAsyncImageLoader.Load(
                            modifier = Modifier
                                .clip(AppTheme.shapes.radiusCircle)
                                .size(AppTheme.dimens.dp64),
                            data = resourcesR.drawable.temp_img_profile_women,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(AppTheme.dimens.dp28)
                            .background(
                                color = AppTheme.colors.generalColors.backgroundPrimary,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clip(AppTheme.shapes.radiusCircle),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = AppTheme.colors.generalColors.primary,
                                    shape = AppTheme.shapes.radiusCircle,
                                )
                                .clip(AppTheme.shapes.radiusCircle)
                                .size(AppTheme.dimens.dp24),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(resourcesR.drawable.img_radio_button_selected_check),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp18),
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = AppTheme.colors.generalColors.foregroundWhite,
                        shape = RoundedCornerShape(
                            topStart = AppTheme.dimens.dp16,
                            topEnd = AppTheme.dimens.dp16,
                            bottomStart = AppTheme.dimens.dp0,
                            bottomEnd = AppTheme.dimens.dp0,
                        ),
                    ),
            ) {
                if (state.route.model.isEdit) {
                    Text(
                        text = "Tebrikler! Grubunu güncelledin", // TODO: Localize
                        style = AppTheme.typography.heading05,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.dimens.dp42),
                        textAlign = TextAlign.Center,
                    )
                } else if (state.route.model.clubName != null) {
                    Text(
                        text = state.route.model.clubName ?: "-", // TODO: Localize
                        style = AppTheme.typography.heading05,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.dimens.dp42),
                        textAlign = TextAlign.Center,
                    )
                }

                Text(
                    text = "Antrenman grubun hazır! Şimdi oyuncularını ve antrenör ekibini davet et ve güçlü bir topluluk oluşturmaya başla. \n" +
                        "\n" +
                        "Birlikte başarıya ulaşmak için takım ruhunu yakala! \uD83D\uDE80", // TODO: Localize
                    style = AppTheme.typography.bodyMediumCompact,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.spacing.spacingHuge),
                    textAlign = TextAlign.Center,
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f),
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.spacing.spacingHuge)
                        .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding()),
                    text = "Grup Üyelerini Davet Et", // TODO: Localize
                    onClick = {
                        setEvent(SuccessAddTrainingGroupScreenContract.Event.NavigateToInviteGroupMembersScreen)
                    },
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SuccessAddTrainingGroupScreenContent(
                state = SuccessAddTrainingGroupScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSuccessAddTrainingGroupRoute(
                        model = HomeScreenSuccessAddTrainingGroupScreenNavigationModel(
                            clubId = null,
                            clubName = null,
                            clubLogo = null,
                            groupId = null,
                            groupName = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
