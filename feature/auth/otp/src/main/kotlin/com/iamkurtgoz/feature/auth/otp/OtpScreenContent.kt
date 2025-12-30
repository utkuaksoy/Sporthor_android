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
package com.iamkurtgoz.feature.auth.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.iamkurtgoz.core.common.extensions.applyMaskedPhoneNumber
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.otpfield.AppOtpField
import com.iamkurtgoz.core.commonui.component.timer.CountDownTimer
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun OtpScreenContent(
    state: OtpScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (OtpScreenContract.Event) -> Unit,
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
                        setEvent.invoke(OtpScreenContract.Event.NavigateUp)
                    },
                )
            }
        }

        item {
            Text(
                text = stringResource(resourcesR.string.otpscreen_label_otp_verification_code),
                style = AppTheme.typography.heading04,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
            )
        }

        item {
            Text(
                text = stringResource(resourcesR.string.otpscreen_label_otp_verification_code_sub_title_with_args, state.navigateRoute.model.phoneNumber.applyMaskedPhoneNumber()),
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                color = AppTheme.colors.generalColors.textSecondary,
            )
        }

        item {
            AppOtpField.Primary(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingLarge)
                    .padding(horizontal = AppTheme.spacing.spacingHuge),
                onValueChange = {
                    setEvent.invoke(OtpScreenContract.Event.SetOtpValue(it))
                },
                enabled = state.timerIsActive,
                isError = state.textOtpValue.isError && state.isFieldErrorShow,
                hint = stringResource(resourcesR.string.warning_label_please_enter_valid_otp_code),
            )
        }

        item {
            CountDownTimer.Primary(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingLarge),
                totalTime = OtpScreenContract.Static.DEFAULT_REMAINING_TIME,
                remainingTime = state.remainingTime,
                warningThreshold = OtpScreenContract.Static.DEFAULT_WARNING_THRESHOLD_TIME,
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .clip(
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .clickable(enabled = !state.timerIsActive) {
                            setEvent.invoke(OtpScreenContract.Event.ReSendOtpCode)
                        },
                ) {
                    Text(
                        text = stringResource(resourcesR.string.button_send_otp_code_again),
                        modifier = Modifier
                            .padding(horizontal = AppTheme.spacing.spacingSmall)
                            .padding(vertical = AppTheme.spacing.spacingSmallest),
                        style = AppTheme.typography.labelMedium,
                        color = if (state.timerIsActive) AppTheme.colors.generalColors.textDisabled else AppTheme.colors.generalColors.textPrimary,
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
            OtpScreenContent(
                state = OtpScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = OtpScreenContract.Fake.fakeNavigateRoute,
                ),
                setEvent = { },
            )
        }
    }
}
