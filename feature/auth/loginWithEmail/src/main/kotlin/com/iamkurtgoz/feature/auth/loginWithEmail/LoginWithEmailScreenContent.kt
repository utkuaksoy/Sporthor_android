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
package com.iamkurtgoz.feature.auth.loginWithEmail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourceR

@Composable
internal fun LoginWithEmailScreenContent(
    state: LoginWithEmailScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (LoginWithEmailScreenContract.Event) -> Unit,
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
                    icon = resourceR.drawable.img_back_arrow,
                    onClick = {
                        setEvent.invoke(LoginWithEmailScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourceR.string.loginwithemailscreen_label_title),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = stringResource(resourceR.string.loginwithemailscreen_label_sub_title),
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textSecondary,
            )
        }

        item {
            AppTextField.Primary(
                title = stringResource(resourceR.string.loginwithemailscreen_label_email_address),
                placeholder = stringResource(resourceR.string.loginwithemailscreen_label_email_address),
                value = state.textEmailAddress.value,
                onValueChange = {
                    setEvent.invoke(LoginWithEmailScreenContract.Event.SetEmailAddress(it))
                },
                isError = state.textEmailAddress.isError && state.isFieldErrorShow,
                hint = stringResource(resourceR.string.warning_label_please_enter_valid_email_address),
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
            AppTextField.Primary(
                title = stringResource(resourceR.string.loginwithemailscreen_label_password),
                placeholder = stringResource(resourceR.string.loginwithemailscreen_label_password),
                value = state.textPassword.value,
                onValueChange = {
                    setEvent.invoke(LoginWithEmailScreenContract.Event.SetPassword(it))
                },
                isError = state.textPassword.isError && state.isFieldErrorShow,
                hint = stringResource(resourceR.string.warning_label_please_enter_valid_password),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                ),
                trailingIcon = resourceR.drawable.img_eye_slash,
                trailingIconClick = {
                    setEvent.invoke(LoginWithEmailScreenContract.Event.ChangePasswordVisualTransformation)
                },
                visualTransformation = state.textPassword.visualTransformation,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = AppTheme.shapes.radiusCircle)
                        .clickable {
                            setEvent.invoke(LoginWithEmailScreenContract.Event.NavigateToForgotPassword)
                        },
                ) {
                    Text(
                        text = stringResource(resourceR.string.loginwithemailscreen_label_forget_password),
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingSmall),
                        style = AppTheme.typography.labelRegular,
                        color = AppTheme.colors.generalColors.textSecondary,
                        textDecoration = TextDecoration.Underline,
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
            LoginWithEmailScreenContent(
                state = LoginWithEmailScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
