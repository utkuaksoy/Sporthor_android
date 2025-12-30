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
package com.iamkurtgoz.feature.home.profile.profileEdit.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.profileEdit.ProfileEditScreenContract

@Composable
internal fun ProfileEditTopRow(
    imageUrlData: Any?,
    setEvent: (ProfileEditScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserImageView(
            data = imageUrlData,
            size = AppTheme.dimens.dp72,
        )

        Text(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingMedium)
                .clickable {
                    setEvent.invoke(ProfileEditScreenContract.Event.SetShowStatePhotoPicker(true))
                },
            text = "Profil fotoğrafını güncelle", // TODO: Localize
            style = AppTheme.typography.subtitleSmall,
            color = AppTheme.colors.generalColors.primitivesBlue600,
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingHuge),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileEditTopRow(
                imageUrlData = "https://randomuser.me/api/portraits/men/32.jpg",
                setEvent = {},
            )
        }
    }
}
