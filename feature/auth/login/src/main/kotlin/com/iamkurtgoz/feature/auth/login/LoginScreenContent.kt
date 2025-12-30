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
package com.iamkurtgoz.feature.auth.login

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R
import com.iamkurtgoz.core.resources.R as resourcesR
import java.util.Locale

@Composable
internal fun LoginScreenContent(
    state: LoginScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (LoginScreenContract.Event) -> Unit,
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
                        setEvent.invoke(LoginScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourcesR.string.loginscreen_label_title),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = "Sistemde kayıtlı e-mail adresiniz ve şifrenizle giriş yapabilirsiniz.", // TODO: Locolize
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textSecondary,
            )
        }

        item {
            AppTextField.Primary(
                title = stringResource(resourcesR.string.authusernamescreen_label_user_name),
                placeholder = stringResource(resourcesR.string.authusernamescreen_label_user_name),
                value = state.textUserName.value,
                onValueChange = {
                    val userName = it.lowercase(Locale.getDefault())
                    setEvent.invoke(LoginScreenContract.Event.SetUserName(userName))
                },
                isError = state.textUserName.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_user_name),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            val trailingIcon: Int = remember(state.textPassword.visualTransformation) {
                if (state.textPassword.visualTransformation is PasswordVisualTransformation) {
                    resourcesR.drawable.img_eye_slash
                } else {
                    resourcesR.drawable.img_eye
                }
            }
            AppTextField.Primary(
                title = stringResource(R.string.loginwithemailscreen_label_password),
                placeholder = stringResource(R.string.loginwithemailscreen_label_password),
                value = state.textPassword.value,
                onValueChange = {
                    setEvent.invoke(LoginScreenContract.Event.SetPassword(it))
                },
                isError = state.textPassword.isError && state.isFieldErrorShow,
                hint = stringResource(R.string.warning_label_please_enter_valid_password),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                ),
                trailingIcon = trailingIcon,
                trailingIconClick = {
                    setEvent.invoke(LoginScreenContract.Event.ChangePasswordVisualTransformation)
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
                            setEvent.invoke(LoginScreenContract.Event.NavigateToForgotPassword)
                        },
                ) {
                    Text(
                        text = stringResource(R.string.loginwithemailscreen_label_forget_password),
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

/*
@Composable
private fun DefaultLoginButtons(setEvent: (LoginScreenContract.Event) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.spacingHuge)
            .padding(top = AppTheme.spacing.spacingHuge),
    ) {
        AppButton.SecondaryLarge(
            text = stringResource(resourcesR.string.button_login_with_email_address),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.spacing.spacingMedium),
            enabled = true,
            onClick = {
                setEvent.invoke(LoginScreenContract.Event.NavigateToLoginWithEmail)
            },
        )

        AppButton.OutlineLarge(
            text = stringResource(resourcesR.string.button_continue_with_google_account),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.spacing.spacingMedium),
            leftIcon = resourcesR.drawable.img_google_logo,
            colors = AppButtonColors.outlineColors(
                leftIconEnabledColor = null,
            ),
            sizes = AppButtonSizes.outlineLargeSizes(
                iconLeadingPadding = AppTheme.dimens.dp18,
            ),
            enabled = true,
            onClick = {
                setEvent.invoke(LoginScreenContract.Event.LoginWithGoogle)
            },
        )

        AppButton.OutlineLarge(
            text = stringResource(resourcesR.string.button_continue_with_facebook_account),
            modifier = Modifier
                .fillMaxWidth(),
            leftIcon = resourcesR.drawable.img_facebook_logo,
            colors = AppButtonColors.outlineColors(
                leftIconEnabledColor = null,
            ),
            sizes = AppButtonSizes.outlineLargeSizes(
                iconLeadingPadding = AppTheme.dimens.dp18,
            ),
            enabled = true,
            onClick = {
                // yönlendirme yapılacak.
            },
        )
    }
}
 */

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            LoginScreenContent(
                state = LoginScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
