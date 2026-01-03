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
package com.iamkurtgoz.feature.home.createTeam

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenCreateTeamRoute
import java.util.Locale
import com.iamkurtgoz.core.resources.R as resourcesR

@Suppress("LongMethod")
@Composable
internal fun CreateTeamScreenContent(
    state: CreateTeamScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (CreateTeamScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            AppCircleButton.SecondaryGrayLarge(
                icon = resourcesR.drawable.img_back_arrow,
                onClick = {
                    setEvent.invoke(CreateTeamScreenContract.Event.NavigateUp)
                },
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Kulübünü Oluştur", // TODO: Localize
                style = AppTheme.typography.heading04,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingRegular)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Kulüp bilgilerini girerek oluştur", // TODO: Localize
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
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(start = AppTheme.spacing.spacingHuge),
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .clip(AppTheme.shapes.radiusCircle)
                            .size(AppTheme.dimens.dp72)
                            .border(
                                width = AppTheme.dimens.dp2,
                                color = if (state.selectedImage == null) AppTheme.colors.generalColors.primary else AppTheme.colors.generalColors.foregroundDisabled,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clickable {
                                setEvent.invoke(CreateTeamScreenContract.Event.SetShowStatePhotoPicker(true))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (state.selectedImage == null) {
                            Box(
                                modifier = Modifier
                                    .padding(all = AppTheme.spacing.spacingSmallest)
                                    .size(AppTheme.dimens.dp72)
                                    .background(
                                        color = AppTheme.colors.generalColors.green100,
                                        shape = AppTheme.shapes.radiusCircle,
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(resourcesR.drawable.img_add_image_plus),
                                    contentDescription = null,
                                )
                            }
                        } else {
                            AppAsyncImageLoader.Load(
                                modifier = Modifier
                                    .clip(AppTheme.shapes.radiusCircle)
                                    .size(AppTheme.dimens.dp68),
                                data = state.selectedImage,
                            )
                        }
                    }

                    if (state.selectedImage != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
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
                                    painter = painterResource(resourcesR.drawable.img_refresh),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(AppTheme.dimens.dp18),
                                )
                            }
                        }
                    }
                }

                Text(
                    text = if (state.selectedImage == null) "Kulüp Logosu Ekle" else "Kulüp Logosunu Değiştir", // TODO: Localize
                    style = AppTheme.typography.subtitleLarge,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingMedium)
                        .clickable {
                            setEvent.invoke(CreateTeamScreenContract.Event.SetShowStatePhotoPicker(true))
                        },
                )
            }
        }

        item {
            AppTextField.Primary(
                title = "Kulüp Adı", // TODO: Localize
                placeholder = "Kulüp adı girin", // TODO: Localize
                value = state.textClubName.value,
                onValueChange = {
                    val userName = it.lowercase(Locale.getDefault())
                    setEvent.invoke(CreateTeamScreenContract.Event.SetClubName(userName))
                },
                isError = state.textClubName.isError && state.isFieldErrorShow,
                hint = "Lütfen geçerli bir kulüp adını girin", // TODO: Localize
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            BasicText(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.spacing.spacingSmall),
                text = "Kulüp Adresi", // TODO: Localize
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
                        setEvent.invoke(CreateTeamScreenContract.Event.NavigateToSelectAddress)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                ) {
                    Text(
                        text = state.textAddressTitle ?: "Adres seçiniz", // TODO: Localize
                        style = AppTheme.typography.bodyLargeCompact,
                        color = if (state.textAddressTitle == null) {
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
                placeholder = "Açık Adres", // TODO: Localize
                value = state.textAddressDetailName.value,
                onValueChange = {
                    val userName = it.lowercase(Locale.getDefault())
                    setEvent.invoke(CreateTeamScreenContract.Event.SetAddressDetailName(userName))
                },
                isError = state.textAddressDetailName.isError && state.isFieldErrorShow,
                hint = "Lütfen geçerli bir açık adres girin", // TODO: Localize
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            BasicText(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.spacing.spacingSmall),
                text = "Branş", // TODO: Localize
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
                        setEvent.invoke(CreateTeamScreenContract.Event.NavigateToCreateTeamSelectBranchScreen)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                ) {
                    Text(
                        text = state.textSelectedBranch?.name ?: "Branş seçiniz", // TODO: Localize
                        style = AppTheme.typography.bodyLargeCompact,
                        color = if (state.textAddressTitle == null) {
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
                title = "Kuruluş Tarihi (Opsiyonel)", // TODO: Localize
                placeholder = "Kuruluş yılı (Opsiyonel)", // TODO: Localize
                value = state.textClubCreateYear.value,
                onValueChange = {
                    val userName = it.lowercase(Locale.getDefault())
                    setEvent.invoke(CreateTeamScreenContract.Event.SetClubCreateYear(userName))
                },
                isError = state.textClubCreateYear.isError && state.isFieldErrorShow,
                hint = "Lütfen geçerli bir yıl girin", // TODO: Localize
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
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
            CreateTeamScreenContent(
                state = CreateTeamScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenCreateTeamRoute(
                        fromGenerateClub = false,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
