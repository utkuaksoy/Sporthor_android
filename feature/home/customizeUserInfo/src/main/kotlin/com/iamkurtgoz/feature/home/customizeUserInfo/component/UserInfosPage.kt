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
package com.iamkurtgoz.feature.home.customizeUserInfo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.GenderType
import com.iamkurtgoz.feature.home.customizeUserInfo.CustomizeUserInfoScreenContract

@Composable
internal fun UserInfosPage(
    state: CustomizeUserInfoScreenContract.State,
    setEvent: (CustomizeUserInfoScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density: Density = LocalDensity.current
    var dynamicWidth by remember { mutableStateOf(AppDefaults.ZERO.dp) }

    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_title),
            style = AppTheme.typography.heading04,
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingLarge)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
        )

        Text(
            text = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_sub_title),
            style = AppTheme.typography.heading06,
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingHuge)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
        )

        Row(
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingLarge)
                .padding(horizontal = AppTheme.spacing.spacingHuge)
                .fillMaxWidth()
                .onGloballyPositioned {
                    dynamicWidth = with(density) {
                        it.size.width.toDp()
                    }
                },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AppTextField.Primary(
                title = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_day),
                placeholder = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_day),
                value = state.textBirthdayDay.value,
                onValueChange = {
                    setEvent.invoke(CustomizeUserInfoScreenContract.Event.SetBirthdayDay(it))
                },
                isError = state.textBirthdayDay.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_birhday_day),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None,
                ),
                maxLength = AppDefaults.DAY_MAX_LENGTH,
                modifier = Modifier
                    .width(width = dynamicWidth.div(AppDefaults.THREE) - AppTheme.spacing.spacingSmall),
            )

            AppTextField.Primary(
                title = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_month),
                placeholder = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_month),
                value = state.textBirthdayMonth.value,
                onValueChange = {
                    setEvent.invoke(CustomizeUserInfoScreenContract.Event.SetBirthdayMonth(it))
                },
                isError = state.textBirthdayMonth.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_birhday_month),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None,
                ),
                maxLength = AppDefaults.MONTH_MAX_LENGTH,
                modifier = Modifier
                    .width(width = dynamicWidth.div(AppDefaults.THREE) - AppTheme.spacing.spacingSmall),
            )

            AppTextField.Primary(
                title = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_year),
                placeholder = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_year),
                value = state.textBirthdayYear.value,
                onValueChange = {
                    setEvent.invoke(CustomizeUserInfoScreenContract.Event.SetBirthdayYear(it))
                },
                isError = state.textBirthdayYear.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_birhday_year),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                ),
                maxLength = AppDefaults.YEAR_MAX_LENGTH,
                modifier = Modifier
                    .width(width = dynamicWidth.div(AppDefaults.THREE) - AppTheme.spacing.spacingSmall),
            )
        }

        Text(
            text = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_gender),
            style = AppTheme.typography.heading06,
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingLarge)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
        )

        AppRadioButton.Primary(
            selected = state.selectedGenderType == GenderType.Woman,
            rightContent = {
                Text(
                    text = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_gender_woman),
                    style = AppTheme.typography.bodyLargeCompact,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmall),
                )
            },
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingMedium)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
            onClick = {
                setEvent.invoke(CustomizeUserInfoScreenContract.Event.SetSelectedGenderType(GenderType.Woman))
            },
        )

        AppRadioButton.Primary(
            selected = state.selectedGenderType == GenderType.Man,
            rightContent = {
                Text(
                    text = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_gender_man),
                    style = AppTheme.typography.bodyLargeCompact,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmall),
                )
            },
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingSmall)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
            onClick = {
                setEvent.invoke(CustomizeUserInfoScreenContract.Event.SetSelectedGenderType(GenderType.Man))
            },
        )

        AppRadioButton.Primary(
            selected = state.selectedGenderType == GenderType.PreferNotToSay,
            rightContent = {
                Text(
                    text = stringResource(resourcesR.string.customizeuserinfoscreen_label_user_info_gender_secret),
                    style = AppTheme.typography.bodyLargeCompact,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmall),
                )
            },
            modifier = Modifier
                .padding(top = AppTheme.spacing.spacingSmall)
                .padding(horizontal = AppTheme.spacing.spacingHuge),
            onClick = {
                setEvent.invoke(CustomizeUserInfoScreenContract.Event.SetSelectedGenderType(GenderType.PreferNotToSay))
            },
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            UserInfosPage(
                state = CustomizeUserInfoScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = {},
            )
        }
    }
}
