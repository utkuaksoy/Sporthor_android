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
package com.iamkurtgoz.feature.home.successDocumentUploadScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSuccessDocumentUploadRoute
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SuccessDocumentUploadScreenScreenContent(
    state: SuccessDocumentUploadScreenScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SuccessDocumentUploadScreenScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.isLoading) {
            item {}
        }

        item {
            Image(
                painter = painterResource(resourcesR.drawable.img_document_upload_success),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingLarge)
                    .size(140.dp)
                    .clickable {
                        setEvent.invoke(SuccessDocumentUploadScreenScreenContract.Event.Initialize)
                    },
            )
        }

        item {
            Text(
                text = "Belgelerini inceleyeceğiz", // TODO: Localize
                style = AppTheme.typography.heading05,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingLarge),
            )
        }

        item {
            Text(
                text = "Belgelerin incelenecek. Uygun bulunması halinde profilinde gerekli izinler ve yetkilendirmeler sağlanacaktır.\n" +
                    "\n" +
                    "Doğrulama tamamlandığında sana bildirim göndereceğiz.\n" +
                    "\n" +
                    "Ayrıca, belgelerini Profil Ayarları bölümünden görüntüleyebilirsin.", // TODO: Localize
                style = AppTheme.typography.bodyMediumCompact,
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(top = AppTheme.spacing.spacingLarge),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SuccessDocumentUploadScreenScreenContent(
                state = SuccessDocumentUploadScreenScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSuccessDocumentUploadRoute(
                        model = HomeScreenSuccessDocumentUploadScreenNavigateModel(
                            clubId = null,
                            founderUserId = null,
                            clubName = null,
                            logo = null,
                        ),
                    )
                ),
                setEvent = { },
            )
        }
    }
}
