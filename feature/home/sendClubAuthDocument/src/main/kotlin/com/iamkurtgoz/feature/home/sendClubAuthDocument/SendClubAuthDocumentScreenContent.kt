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
package com.iamkurtgoz.feature.home.sendClubAuthDocument

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.DocumentUploadSection
import com.iamkurtgoz.core.commonui.component.SelectedClubSection
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSendClubAuthDocumentRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SendClubAuthDocumentScreenContent(
    state: SendClubAuthDocumentScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SendClubAuthDocumentScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            AppCircleButton.SecondaryGrayLarge(
                icon = resourcesR.drawable.img_back_arrow,
                onClick = {
                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.NavigateUp)
                },
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Kulüp yetki belgesi gönder", // TODO: Localize
                style = AppTheme.typography.heading04,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingRegular)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Seçtiğin kulüp için doğrulama yapman gerekiyor. Kulüp yetki belgelerini yükleyin", // TODO: Localize
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
                clubName = state.route.model.clubName ?: "-",
                clubLogo = state.route.model.logo ?: "",
                onChangeClick = {
                },
            )
        }
        
        item {
            DocumentUploadSection(
                documentList = state.documentList,
                onSelectDocumentClick = { index ->
                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.OnSelectDocumentClick(index))
                },
                onAddNewDocumentClick = {
                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.AddNewDocumentClick)
                },
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SendClubAuthDocumentScreenContent(
                state = SendClubAuthDocumentScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSendClubAuthDocumentRoute(
                        model = HomeScreenSendClubAuthDocumentScreenNavigateModel(
                            clubId = null,
                            founderUserId = null,
                            clubName = null,
                            logo = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
