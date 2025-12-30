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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.profileEdit.ProfileEditScreenContract
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.types.InfoRowKeyboardType
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.types.InfoRowType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun ProfileEditInfoRow(
    title: String?,
    appTextFieldValue: AppTextFieldValue?,
    type: InfoRowType?,
    parameterName: String?,
    setEvent: (ProfileEditScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    var isGetLocationClicked by remember { mutableStateOf(false) }

    LaunchedEffect(locationPermissionState.status.isGranted, isGetLocationClicked) {
        if (locationPermissionState.status.isGranted && isGetLocationClicked) {
            setEvent.invoke(ProfileEditScreenContract.Event.GetLocation)
            isGetLocationClicked = false
        } else {
            locationPermissionState.launchPermissionRequest()
            isGetLocationClicked = false
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingMedium)
            .padding(start = AppTheme.spacing.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_0_35)
                .padding(end = AppTheme.spacing.spacingLarge),
            text = title ?: "",
        )

        BasicTextField(
            value = appTextFieldValue?.value ?: "",
            onValueChange = { input ->
                val value = when (type) {
                    InfoRowType.DATE -> input.filter { it.isDigit() }.take(AppDefaults.EIGHT)
                    else -> input
                }

                val updatedAppTextFieldValue = appTextFieldValue?.copy(value = value)
                updatedAppTextFieldValue?.let {
                    setEvent.invoke(ProfileEditScreenContract.Event.SetDynamicTextFieldValue(updatedAppTextFieldValue))
                }

                coroutineScope.launch {
                    delay(ProfileEditScreenContract.Static.DELAY_100_MILLISECOND)
                    bringIntoViewRequester.bringIntoView()
                }
            },
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_0_65)
                .padding(end = AppTheme.spacing.spacingMedium)
                .bringIntoViewRequester(bringIntoViewRequester)
                .focusTarget()
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .then(
                    if (parameterName == "city") {
                        Modifier
                            .clickable {
                                isGetLocationClicked = true
                            }
                    } else Modifier,
                ),
            textStyle = AppTheme.typography.bodyMediumCompact,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = when (type?.keyboardType) {
                    InfoRowKeyboardType.NORMAL -> KeyboardType.Text
                    InfoRowKeyboardType.NUMBER -> KeyboardType.Number
                    else -> KeyboardType.Text
                },
            ),
            visualTransformation = when (type) {
                InfoRowType.DATE -> MaskVisualTransformation()
                else -> VisualTransformation.None
            },
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier.height(AppTheme.dimens.dp42),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Spacer(modifier = Modifier.height(AppTheme.dimens.dp1))

                    Row {
                        innerTextField()

                        /*
                        type?.extension?.let {
                            Text(
                                text = it,
                                style = AppTheme.typography.bodyMediumCompact,
                            )
                        }
                         */
                    }

                    HorizontalDivider(thickness = AppTheme.dimens.dp1)
                }
            },
            enabled = parameterName != "city",
        )
    }
}

class MaskVisualTransformation(
    private val mask: String = "##.##.####",
    private val maskChar: Char = '#',
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }
        val result = StringBuilder()
        var digitIndex = 0

        for (maskIndex in mask.indices) {
            if (digitIndex >= digits.length) break

            val maskCharAt = mask[maskIndex]
            if (maskCharAt == maskChar) {
                result.append(digits[digitIndex])
                digitIndex++
            } else {
                result.append(maskCharAt)
            }
        }

        val transformed = result.toString()
        return TransformedText(
            AnnotatedString(transformed),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    var digitsSeen = 0
                    for (i in mask.indices) {
                        if (digitsSeen == offset) return i.coerceAtMost(transformed.length)
                        if (mask[i] == maskChar) digitsSeen++
                    }
                    return transformed.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    var digitsSeen = 0
                    for (i in 0 until offset.coerceAtMost(mask.length)) {
                        if (mask[i] == maskChar) digitsSeen++
                    }
                    return digitsSeen.coerceAtMost(text.length)
                }
            },
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfileEditInfoRow(
                title = "Title",
                appTextFieldValue = AppTextFieldValue(
                    value = "22081997",
                ),
                type = InfoRowType.DATE,
                parameterName = "Istanbul",
                setEvent = {},
            )
        }
    }
}
