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
package com.iamkurtgoz.feature.auth.forgetPassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.radiobutton.AppRadioButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun ForgetPasswordScreenContent(
    state: ForgetPasswordScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (ForgetPasswordScreenContract.Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = AppTheme.spacing.spacingLarge),
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
                        setEvent.invoke(ForgetPasswordScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourcesR.string.forgetpasswordscreen_label_title),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = stringResource(resourcesR.string.forgetpasswordscreen_label_sub_title),
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textSecondary,
            )
        }

        item {
            AppTextField.Primary(
                title = stringResource(resourcesR.string.authusernamescreen_label_user_name_title),
                placeholder = stringResource(resourcesR.string.authusernamescreen_label_user_name_title),
                value = state.textUserName.value,
                onValueChange = {
                    setEvent.invoke(ForgetPasswordScreenContract.Event.SetEmailAddress(it))
                },
                isError = state.textUserName.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_email_address),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.None,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            ) {
                AppRadioButton.Primary(
                    selected = state.forgotPasswordType == AppDefaults.ZERO,
                    onClick = {
                        setEvent.invoke(ForgetPasswordScreenContract.Event.SetForgotPasswordType(AppDefaults.ZERO))
                    },
                    rightContent = {
                        Text(
                            text = "Mail",
                            style = AppTheme.typography.bodyLargeCompact,
                            color = AppTheme.colors.generalColors.textPrimary,
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingSmall),
                        )
                    },
                )

                AppRadioButton.Primary(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingSmall),
                    selected = state.forgotPasswordType == AppDefaults.ONE,
                    onClick = {
                        setEvent.invoke(ForgetPasswordScreenContract.Event.SetForgotPasswordType(AppDefaults.ONE))
                    },
                    rightContent = {
                        Text(
                            text = "SMS",
                            style = AppTheme.typography.bodyLargeCompact,
                            color = AppTheme.colors.generalColors.textPrimary,
                            modifier = Modifier
                                .padding(start = AppTheme.spacing.spacingSmall),
                        )
                    },
                )
            }
        }

        item {
            AppButton.PrimaryLarge(
                text = stringResource(resourcesR.string.button_send_reset_password_mail),
                enabled = state.buttonActive,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                onClick = {
                    setEvent.invoke(ForgetPasswordScreenContract.Event.SendResetPasswordMail)
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
            ForgetPasswordScreenContent(
                state = ForgetPasswordScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
