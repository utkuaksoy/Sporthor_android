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
package com.iamkurtgoz.feature.auth.userName

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.horizontalListButton.AppHorizontalListButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.AuthUserNameScreenRoute
import com.iamkurtgoz.core.navigation.model.auth.userName.fakeAuthUserNameScreenNavigateModel
import com.iamkurtgoz.feature.auth.userName.domain.model.CheckUserNameUIModel
import kotlinx.collections.immutable.toImmutableList
import java.util.Locale
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun UserNameScreenContent(
    state: UserNameScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (UserNameScreenContract.Event) -> Unit,
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
                        setEvent.invoke(UserNameScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourcesR.string.authusernamescreen_label_user_name_title),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = stringResource(resourcesR.string.authusernamescreen_label_user_name_sub_title),
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
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
                    setEvent.invoke(UserNameScreenContract.Event.SetUserName(userName))
                },
                isError = state.textUserName.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_user_name),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        when {
            state.textUserName.value.isEmpty() || state.textUserName.value.length < UserNameScreenContract.Static.MIN_USER_NAME_LENGTH || state.textUserName.value.length > UserNameScreenContract.Static.MAX_USER_NAME_LENGTH -> {
                item {
                    Text(
                        text = stringResource(resourcesR.string.warning_label_user_name_rules),
                        style = AppTheme.typography.labelRegular,
                        color = AppTheme.colors.generalColors.textError800,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.spacing.spacingSmall)
                            .padding(horizontal = AppTheme.spacing.spacingHuge),
                    )
                }
            }
            state.checkUserNameModel != null -> {
                state.checkUserNameModel.isUsable?.let { isUsable ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = AppTheme.spacing.spacingSmall)
                                .padding(horizontal = AppTheme.spacing.spacingHuge),
                        ) {
                            Image(
                                painter = painterResource(if (isUsable) resourcesR.drawable.img_check_circle else resourcesR.drawable.img_alert_circle),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp18),
                            )

                            Text(
                                text = "${state.textUserName.value} ${if (isUsable) "kullanıcı adıyla devam edebilirsin" else "başkası tarafından alınmış"}", // TODO: Localize
                                style = AppTheme.typography.labelRegular,
                                color = AppTheme.colors.generalColors.textPrimary,
                                modifier = Modifier
                                    .padding(start = AppTheme.spacing.spacingSmallest),
                            )
                        }
                    }
                }
            }
            state.backendErrorMessage != null -> {
                item {
                    Text(
                        text = state.backendErrorMessage,
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

        state.checkUserNameModel?.suggestions?.toImmutableList()?.let { suggestions ->
            if (suggestions.isNotEmpty()) {
                item {
                    Text(
                        text = "Kullanıcı adı önerileri", // TODO: Localize
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colors.generalColors.textSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.spacing.spacingHuge)
                            .padding(horizontal = AppTheme.spacing.spacingHuge),
                    )
                }

                item {
                    AppHorizontalListButton.Primary(
                        modifier = Modifier
                            .padding(top = AppTheme.spacing.spacingSmall),
                        itemList = suggestions,
                        customPadding = { index ->
                            PaddingValues(
                                start = if (index == AppDefaults.ZERO) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingSmall,
                                end = if (index == suggestions.lastIndex) AppTheme.spacing.spacingHuge else AppTheme.spacing.spacingNone,
                            )
                        },
                        onClick = {
                            setEvent.invoke(UserNameScreenContract.Event.SetUserNameFromSuggestion(it.first))
                        },
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
            UserNameScreenContent(
                state = UserNameScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    textUserName = AppTextFieldValue(
                        value = "a",
                    ),
                    checkUserNameModel = CheckUserNameUIModel(
                        isUsable = false,
                        suggestions = listOf("user1" to "user1", "user2" to "user2", "user3" to "user3"),
                    ),
                    navigateRoute = AuthUserNameScreenRoute(
                        model = fakeAuthUserNameScreenNavigateModel,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
