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
package com.iamkurtgoz.feature.auth.userInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.iamkurtgoz.core.common.extensions.isPasswordValid
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.AuthUserInfoScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.userInfo.fakeAuthUserInfoScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun UserInfoScreenContent(
    state: UserInfoScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (UserInfoScreenContract.Event) -> Unit,
) {
    val density: Density = LocalDensity.current

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
                        setEvent.invoke(UserInfoScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourcesR.string.authuserinfoscreen_label_user_info_title),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = stringResource(resourcesR.string.authuserinfoscreen_label_user_info_sub_title),
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textSecondary,
            )
        }

        item {
            var dynamicWidth by remember { mutableStateOf(AppDefaults.ZERO.dp) }

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
                    title = stringResource(resourcesR.string.authuserinfoscreen_label_first_name),
                    placeholder = stringResource(resourcesR.string.authuserinfoscreen_label_first_name),
                    value = state.textFirstName.value,
                    onValueChange = {
                        setEvent.invoke(UserInfoScreenContract.Event.SetFirstName(it))
                    },
                    isError = state.textFirstName.isError && state.isFieldErrorShow,
                    hint = stringResource(resourcesR.string.warning_label_please_enter_valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words,
                    ),
                    modifier = Modifier
                        .width(width = dynamicWidth.div(AppDefaults.TWO) - AppTheme.spacing.spacingSmall),
                )

                AppTextField.Primary(
                    title = stringResource(resourcesR.string.authuserinfoscreen_label_last_name),
                    placeholder = stringResource(resourcesR.string.authuserinfoscreen_label_last_name),
                    value = state.textLastName.value,
                    onValueChange = {
                        setEvent.invoke(UserInfoScreenContract.Event.SetLastName(it))
                    },
                    isError = state.textLastName.isError && state.isFieldErrorShow,
                    hint = stringResource(resourcesR.string.warning_label_please_enter_valid_last_name),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Words,
                    ),
                    modifier = Modifier
                        .width(width = dynamicWidth.div(AppDefaults.TWO) - AppTheme.spacing.spacingSmall),
                )
            }
        }

        item {
            AppTextField.Primary(
                title = stringResource(resourcesR.string.authuserinfoscreen_label_email_address),
                placeholder = stringResource(resourcesR.string.authuserinfoscreen_label_email_address),
                value = state.textEmailAddress.value,
                onValueChange = {
                    setEvent.invoke(UserInfoScreenContract.Event.SetEmailAddress(it))
                },
                isError = state.textEmailAddress.isError && state.isFieldErrorShow,
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
            AppTextField.Primary(
                title = stringResource(resourcesR.string.authuserinfoscreen_label_password),
                placeholder = stringResource(resourcesR.string.authuserinfoscreen_label_password),
                value = state.textPassword.value,
                onValueChange = {
                    setEvent.invoke(UserInfoScreenContract.Event.SetPassword(it))
                },
                isError = state.textPassword.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_password),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.None,
                ),
                trailingIcon = resourcesR.drawable.img_eye_slash,
                trailingIconClick = {
                    setEvent.invoke(UserInfoScreenContract.Event.ChangePasswordVisualTransformation)
                },
                visualTransformation = state.textPassword.visualTransformation,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        if (state.textPassword.value.isPasswordValid()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.spacing.spacingSmall)
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                ) {
                    Text(
                        text = stringResource(resourcesR.string.authuserinfoscreen_password_status),
                        style = AppTheme.typography.labelRegular,
                    )

                    Text(
                        text = stringResource(resourcesR.string.authuserinfoscreen_password_strong),
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colors.generalColors.textSuccess800,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingSmall),
                    )
                }
            }
        } else {
            item {
                Text(
                    text = stringResource(resourcesR.string.warning_label_password_rules),
                    style = AppTheme.typography.labelRegular,
                    color = AppTheme.colors.generalColors.textError800,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.spacing.spacingSmall)
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            UserInfoScreenContent(
                state = UserInfoScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = AuthUserInfoScreenRoute(
                        model = fakeAuthUserInfoScreenNavigateModel,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
