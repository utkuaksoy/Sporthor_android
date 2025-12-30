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
package com.iamkurtgoz.feature.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.iamkurtgoz.core.common.extensions.isNumber
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.countryCodePicker.AppCountryCodePicker
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.designsystem.transformation.PhoneNumberVisualTransformation
import com.iamkurtgoz.core.navigation.AuthRegisterScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.register.fakeAuthRegisterScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun RegisterScreenContent(
    state: RegisterScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (RegisterScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = AppTheme.spacing.spacingLarge,
        ),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                contentAlignment = Alignment.CenterStart,
            ) {
                AppCircleButton.SecondaryGrayLarge(
                    icon = resourcesR.drawable.img_back_arrow,
                    onClick = {
                        setEvent.invoke(RegisterScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourcesR.string.registerscreen_label_lets_start),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = stringResource(resourcesR.string.registerscreen_label_otp_number_info),
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textSecondary,
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingLarge),
            ) {
                AppCountryCodePicker.Primary(
                    icon = resourcesR.drawable.img_tr_flag,
                    countryCode = RegisterScreenContract.Static.COUNTRY_CODE_TR,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingHuge),
                )

                AppTextField.Primary(
                    placeholder = stringResource(resourcesR.string.registerscreen_label_phone_number),
                    isError = state.textPhoneNumber.isError && state.isFieldErrorShow,
                    value = state.textPhoneNumber.value,
                    onValueChange = { newValue ->
                        if (newValue.isNumber() && newValue.length <= RegisterScreenContract.Static.PHONE_NUMBER_LIMIT) {
                            setEvent.invoke(RegisterScreenContract.Event.SetTextPhoneNumber(newValue))
                        }
                    },
                    hint = stringResource(resourcesR.string.warning_label_please_enter_valid_phone_number),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            setEvent.invoke(RegisterScreenContract.Event.SendOtpCode)
                        },
                    ),
                    visualTransformation = PhoneNumberVisualTransformation,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingMedium)
                        .padding(end = AppTheme.spacing.spacingHuge),
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingMedium),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(resourcesR.string.warning_do_you_have_an_account),
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingHuge),
                    style = AppTheme.typography.bodyMediumCompact,
                    color = AppTheme.colors.generalColors.textSecondary,
                )

                Box(
                    modifier = Modifier
                        .clip(
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .clickable {
                            setEvent.invoke(RegisterScreenContract.Event.NavigateToLogin)
                        },
                ) {
                    Text(
                        text = stringResource(resourcesR.string.button_login_button),
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingSmall)
                            .padding(vertical = AppTheme.spacing.spacingSmallest),
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colors.generalColors.textPrimary,
                    )
                }
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            RegisterScreenContent(
                state = RegisterScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = AuthRegisterScreenRoute(
                        model = fakeAuthRegisterScreenNavigateModel,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
