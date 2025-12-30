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
package com.iamkurtgoz.feature.home.profile.profileEdit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastFirstOrNull
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.profile.profileEdit.component.ProfileEditBranchRow
import com.iamkurtgoz.feature.home.profile.profileEdit.component.ProfileEditInfoRow
import com.iamkurtgoz.feature.home.profile.profileEdit.component.ProfileEditTeamRow
import com.iamkurtgoz.feature.home.profile.profileEdit.component.ProfileEditTopRow
import com.iamkurtgoz.feature.home.profile.profileEdit.mock.model.MockEditProfileSummaryUIModel

@Composable
internal fun ProfileEditScreenContent(
    state: ProfileEditScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ProfileEditScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
    ) {

        item {
            ProfileEditTopRow(
                imageUrlData = state.selectedImage ?: state.profileSummaryModel?.profileImage,
                setEvent = setEvent,
            )
        }

        item {
            Text(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(start = AppTheme.spacing.spacingMedium)
                    .clickable {
                        setEvent.invoke(ProfileEditScreenContract.Event.Initialize)
                    },
                text = state.profileSummaryModel?.profileInfo?.title ?: "",
                style = AppTheme.typography.subtitleLarge,
            )
        }

        itemsIndexed(
            items = state.profileSummaryModel?.profileInfo?.row ?: emptyList(),
            key = { index, item ->
                "$index - $item"
            },
            itemContent = { index, item ->
                ProfileEditInfoRow(
                    title = item.title,
                    appTextFieldValue = state.dynamicTextFieldValues.firstOrNull { it.id == item.parameterName },
                    type = item.type,
                    parameterName = item.parameterName,
                    setEvent = setEvent,
                )
            },
        )

        item {
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge),
                thickness = AppTheme.dimens.dp1,
            )
        }

        item {
            Text(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(start = AppTheme.spacing.spacingMedium),
                text = state.profileSummaryModel?.teamInfo?.title ?: "",
                style = AppTheme.typography.subtitleLarge,
            )
        }

        item {
            ProfileEditTeamRow(
                teams = state.profileSummaryModel?.teamInfo?.teams ?: emptyList(),
                teamRowInfo = state.profileSummaryModel?.teamInfo?.info ?: "",
            )
        }

        item {
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge),
                thickness = AppTheme.dimens.dp1,
            )
        }

        item {
            Text(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(start = AppTheme.spacing.spacingMedium),
                text = state.profileSummaryModel?.highlights?.title ?: "",
                style = AppTheme.typography.subtitleLarge,
            )
        }

        item {
            ProfileEditBranchRow(
                state = state,
                setEvent = setEvent,
                branches = state.profileSummaryModel?.highlights?.branches,
            )
        }

        state.profileSummaryModel?.highlights?.branchesAttributes?.fastFirstOrNull {
            it.branchId == state.selectedBranchId
        }?.let { branch ->
            branch.branchInfoRow?.let { items ->
                itemsIndexed(
                    items = items,
                    key = { index, item ->
                        "$index${branch.branchId}${item.text}${item.title}${item.parameterName}${item.type}${item.placeholder}"
                    },
                    itemContent = { index, item ->
                        ProfileEditInfoRow(
                            title = item.title,
                            appTextFieldValue = state.dynamicTextFieldValues.firstOrNull { it.id == branch.branchId.plus(item.parameterName ?: "") },
                            type = item.type,
                            parameterName = item.parameterName,
                            setEvent = setEvent,
                        )
                    },
                )
            }
        }

        item {
            AppButton.PrimaryLarge(
                text = "Değişiklikleri Kaydet", // TODO: Localize
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
                onClick = {
                    setEvent.invoke(ProfileEditScreenContract.Event.UpdateProfileSummary)
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
            ProfileEditScreenContent(
                state = ProfileEditScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    profileSummaryModel = MockEditProfileSummaryUIModel.mockEditProfileSummary,
                ),
                setEvent = { },
            )
        }
    }
}
