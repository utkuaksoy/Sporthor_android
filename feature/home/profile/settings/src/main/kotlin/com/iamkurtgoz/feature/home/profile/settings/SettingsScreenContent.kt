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
package com.iamkurtgoz.feature.home.profile.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SettingsScreenContent(
    state: SettingsScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SettingsScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                        setEvent.invoke(SettingsScreenContract.Event.NavigateToAccountSettings)
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_lock_v2,
                title = "Hesap Ayarları", // TODO: Localize
            )
        }

        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                        setEvent.invoke(SettingsScreenContract.Event.NavigateToAboutUs)
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_align_justify,
                title = "Hakkında", // TODO: Localize
            )
        }

        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_question_circle,
                title = "Yardım Alın", // TODO: Localize
            )
        }

        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_comment_plus_v2,
                title = "İletişim Merkezi", // TODO: Localize
            )
        }

        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                        setEvent(
                            SettingsScreenContract.Event.NavigateToWebView(
                                routeType = HomeScreenWebViewScreenNavigateModel(
                                    title = "Kullanım Şartları",
                                    url = "https://accounts.sporthor.com/Agreement/TermsofUse",
                                ),
                            ),
                        )
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_document,
                title = "Kullanım Şartları", // TODO: Localize
            )
        }

        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                        setEvent(
                            SettingsScreenContract.Event.NavigateToWebView(
                                routeType = HomeScreenWebViewScreenNavigateModel(
                                    title = "Gizlilik Sözleşmesi",
                                    url = "https://accounts.sporthor.com/Agreement/Privacy",
                                ),
                            ),
                        )
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_document,
                title = "Gizlilik Sözleşmesi", // TODO: Localize
            )
        }

        item {
            SettingsScreenRow(
                modifier = Modifier
                    .clickable {
                        setEvent.invoke(SettingsScreenContract.Event.Logout)
                    }
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmall),
                icon = resourcesR.drawable.img_power,
                title = "Oturumu Kapat", // TODO: Localize
                contentColor = AppTheme.colors.generalColors.primitivesRed500,
            )
        }
    }
}

@Composable
private fun SettingsScreenRow(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    contentColor: Color = AppTheme.colors.generalColors.textPrimary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(AppTheme.dimens.dp24),
            colorFilter = ColorFilter.tint(contentColor),
        )

        Text(
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL)
                .padding(start = AppTheme.spacing.spacingSmall),
            text = title,
            style = AppTheme.typography.heading03.copy(
                fontSize = AppTheme.dimens.sp16,
                lineHeight = AppTheme.dimens.sp20,
                color = contentColor,
            ),
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SettingsScreenContent(
                state = SettingsScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
