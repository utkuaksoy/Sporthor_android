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
package com.iamkurtgoz.core.commonui.component.otpfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.isNumber
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold

object AppOtpField {
    @Composable
    fun Primary(
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        hint: String = "",
        enabled: Boolean = true,
        colors: OtpFieldColors = AppOtpFieldColors.primaryColors(),
        sizes: OtpFieldSizes = AppOtpFieldSizes.primarySizes(),
        borders: OtpFieldBorders = AppOtpFieldBorders.primaryBorders(),
        shapes: OtpFieldShapes = AppOtpFieldShapes.primaryShapes(),
        styles: OtpFieldStyles = AppOtpFieldStyles.primaryStyles(),
        isError: Boolean = false,
        readOnly: Boolean = false,
        singleLine: Boolean = true,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        keyboardActionDone: () -> Unit = {},
    ) = AppOtpFieldImpl(
        onValueChange = onValueChange,
        hint = hint,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        sizes = sizes,
        borders = borders,
        shapes = shapes,
        styles = styles,
        isError = isError,
        readOnly = readOnly,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        keyboardActionDone = keyboardActionDone,
    )
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
private fun AppOtpFieldImpl(
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    enabled: Boolean = true,
    colors: OtpFieldColors = AppOtpFieldColors.primaryColors(),
    sizes: OtpFieldSizes = AppOtpFieldSizes.primarySizes(),
    borders: OtpFieldBorders = AppOtpFieldBorders.primaryBorders(),
    shapes: OtpFieldShapes = AppOtpFieldShapes.primaryShapes(),
    styles: OtpFieldStyles = AppOtpFieldStyles.primaryStyles(),
    isError: Boolean = false,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    keyboardActionDone: () -> Unit = {},
) {
    val hintContentColor by colors.hintColor(enabled, isError)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Values
    var otpFieldFirst by remember { mutableStateOf("") }
    var otpFieldSecond by remember { mutableStateOf("") }
    var otpFieldThird by remember { mutableStateOf("") }
    var otpFieldFourth by remember { mutableStateOf("") }
    var otpFieldFifth by remember { mutableStateOf("") }
    var otpFieldSixth by remember { mutableStateOf("") }

    // Focus Requesters
    val otpFieldFirstFocusRequester = remember { FocusRequester() }
    val otpFieldSecondFocusRequester = remember { FocusRequester() }
    val otpFieldThirdFocusRequester = remember { FocusRequester() }
    val otpFieldFourthFocusRequester = remember { FocusRequester() }
    val otpFieldFifthFocusRequester = remember { FocusRequester() }
    val otpFieldSixthFocusRequester = remember { FocusRequester() }

    LaunchedEffect(otpFieldFirst, otpFieldSecond, otpFieldThird, otpFieldFourth, otpFieldFifth, otpFieldSixth) {
        onValueChange.invoke(otpFieldFirst.plus(otpFieldSecond).plus(otpFieldThird).plus(otpFieldFourth).plus(otpFieldFifth).plus(otpFieldSixth))
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppOtpFieldItem(
                value = otpFieldFirst,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        otpFieldFirst = newValue
                    } else if (newValue.length == AppDefaults.ONE) {
                        if (newValue.isNumber()) {
                            otpFieldFirst = newValue
                            otpFieldSecondFocusRequester.requestFocus()
                        }
                    } else if (newValue.length == AppDefaults.TWO && newValue.lastOrNull()?.toString()?.isNumber() == true) {
                        newValue.lastOrNull()?.let { last ->
                            otpFieldFirst = last.toString()
                            otpFieldSecondFocusRequester.requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(otpFieldFirstFocusRequester),
                enabled = enabled,
                colors = colors,
                sizes = sizes,
                borders = borders,
                shapes = shapes,
                styles = styles,
                isError = isError,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true,
                ),
                keyboardActions = KeyboardActions(
                    onPrevious = {
                        keyboardController?.hide()
                        focusManager.clearFocus(true)
                    },
                    onNext = {
                        otpFieldSecondFocusRequester.requestFocus()
                    },
                ),
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
            )

            AppOtpFieldItem(
                value = otpFieldSecond,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        otpFieldSecond = newValue
                        otpFieldFirstFocusRequester.requestFocus()
                    } else if (newValue.length == AppDefaults.ONE) {
                        if (newValue.isNumber()) {
                            otpFieldSecond = newValue
                            otpFieldThirdFocusRequester.requestFocus()
                        }
                    } else if (newValue.length == AppDefaults.TWO && newValue.lastOrNull()?.toString()?.isNumber() == true) {
                        newValue.lastOrNull()?.let { last ->
                            otpFieldSecond = last.toString()
                            otpFieldThirdFocusRequester.requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(otpFieldSecondFocusRequester),
                enabled = enabled,
                colors = colors,
                sizes = sizes,
                borders = borders,
                shapes = shapes,
                styles = styles,
                isError = isError,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true,
                ),
                keyboardActions = KeyboardActions(
                    onPrevious = {
                        otpFieldFirstFocusRequester.requestFocus()
                    },
                    onNext = {
                        otpFieldThirdFocusRequester.requestFocus()
                    },
                ),
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
            )

            AppOtpFieldItem(
                value = otpFieldThird,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        otpFieldThird = newValue
                        otpFieldSecondFocusRequester.requestFocus()
                    } else if (newValue.length == AppDefaults.ONE) {
                        if (newValue.isNumber()) {
                            otpFieldThird = newValue
                            otpFieldFourthFocusRequester.requestFocus()
                        }
                    } else if (newValue.length == AppDefaults.TWO && newValue.lastOrNull()?.toString()?.isNumber() == true) {
                        newValue.lastOrNull()?.let { last ->
                            otpFieldThird = last.toString()
                            otpFieldFourthFocusRequester.requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(otpFieldThirdFocusRequester),
                enabled = enabled,
                colors = colors,
                sizes = sizes,
                borders = borders,
                shapes = shapes,
                styles = styles,
                isError = isError,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true,
                ),
                keyboardActions = KeyboardActions(
                    onPrevious = {
                        otpFieldSecondFocusRequester.requestFocus()
                    },
                    onNext = {
                        otpFieldFourthFocusRequester.requestFocus()
                    },
                ),
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(AppTheme.dimens.dp8),
                thickness = AppTheme.dimens.dp2,
                color = AppTheme.colors.generalColors.foregroundBlack,
            )

            AppOtpFieldItem(
                value = otpFieldFourth,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        otpFieldFourth = newValue
                        otpFieldThirdFocusRequester.requestFocus()
                    } else if (newValue.length == AppDefaults.ONE) {
                        if (newValue.isNumber()) {
                            otpFieldFourth = newValue
                            otpFieldFifthFocusRequester.requestFocus()
                        }
                    } else if (newValue.length == AppDefaults.TWO && newValue.lastOrNull()?.toString()?.isNumber() == true) {
                        newValue.lastOrNull()?.let { last ->
                            otpFieldFourth = last.toString()
                            otpFieldFifthFocusRequester.requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(otpFieldFourthFocusRequester),
                enabled = enabled,
                colors = colors,
                sizes = sizes,
                borders = borders,
                shapes = shapes,
                styles = styles,
                isError = isError,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true,
                ),
                keyboardActions = KeyboardActions(
                    onPrevious = {
                        otpFieldThirdFocusRequester.requestFocus()
                    },
                    onNext = {
                        otpFieldFifthFocusRequester.requestFocus()
                    },
                ),
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
            )

            AppOtpFieldItem(
                value = otpFieldFifth,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        otpFieldFifth = newValue
                        otpFieldFourthFocusRequester.requestFocus()
                    } else if (newValue.length == AppDefaults.ONE) {
                        if (newValue.isNumber()) {
                            otpFieldFifth = newValue
                            otpFieldSixthFocusRequester.requestFocus()
                        }
                    } else if (newValue.length == AppDefaults.TWO && newValue.lastOrNull()?.toString()?.isNumber() == true) {
                        newValue.lastOrNull()?.let { last ->
                            otpFieldFifth = last.toString()
                            otpFieldSixthFocusRequester.requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(otpFieldFifthFocusRequester),
                enabled = enabled,
                colors = colors,
                sizes = sizes,
                borders = borders,
                shapes = shapes,
                styles = styles,
                isError = isError,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true,
                ),
                keyboardActions = KeyboardActions(
                    onPrevious = {
                        otpFieldFourthFocusRequester.requestFocus()
                    },
                    onNext = {
                        otpFieldSixthFocusRequester.requestFocus()
                    },
                ),
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
            )

            AppOtpFieldItem(
                value = otpFieldSixth,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        otpFieldSixth = newValue
                        otpFieldFifthFocusRequester.requestFocus()
                    } else if (newValue.length == AppDefaults.ONE) {
                        if (newValue.isNumber()) {
                            otpFieldSixth = newValue
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                        }
                    } else if (newValue.length == AppDefaults.TWO && newValue.lastOrNull()?.toString()?.isNumber() == true) {
                        newValue.lastOrNull()?.let { last ->
                            otpFieldSixth = last.toString()
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(otpFieldSixthFocusRequester),
                enabled = enabled,
                colors = colors,
                sizes = sizes,
                borders = borders,
                shapes = shapes,
                styles = styles,
                isError = isError,
                readOnly = readOnly,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    showKeyboardOnFocus = true,
                ),
                keyboardActions = KeyboardActions(
                    onPrevious = {
                        otpFieldFifthFocusRequester.requestFocus()
                    },
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus(true)
                        keyboardActionDone.invoke()
                    },
                ),
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                onTextLayout = onTextLayout,
            )
        }

        AnimatedVisibility(visible = isError && hint.isNotEmpty()) {
            BasicText(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingSmallest),
                text = hint,
                style = styles.hintTextStyle,
                color = { hintContentColor },
                maxLines = AppDefaults.LINE_LIMIT_SINGLE,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun AppOtpFieldItem(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: OtpFieldColors = AppOtpFieldColors.primaryColors(),
    sizes: OtpFieldSizes = AppOtpFieldSizes.primarySizes(),
    borders: OtpFieldBorders = AppOtpFieldBorders.primaryBorders(),
    shapes: OtpFieldShapes = AppOtpFieldShapes.primaryShapes(),
    styles: OtpFieldStyles = AppOtpFieldStyles.primaryStyles(),
    isError: Boolean = false,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val borderModifier by borders.borderModifier(enabled, isError, interactionSource)
    val containerColor by colors.containerColor(enabled, isError, interactionSource)
    val contentColor by colors.contentColor(enabled, isError, interactionSource)

    Column(
        modifier = modifier,
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            textStyle = styles.contentTextStyle.copy(
                color = contentColor,
                textAlign = TextAlign.Center,
            ),
            cursorBrush = SolidColor(colors.cursorColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerOtpField ->
                Row(
                    modifier = Modifier
                        .width(sizes.width)
                        .height(sizes.height)
                        .background(
                            color = containerColor,
                            shape = shapes.roundedCornerShape,
                        )
                        .then(borderModifier)
                        .padding(sizes.contentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    innerOtpField()
                }
            },
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    var value: String by remember { mutableStateOf("3") }
    AppTheme {
        AppThemeScaffold {
            Column(
                modifier = Modifier
                    .padding(all = AppTheme.spacing.spacingMedium),
            ) {
                AppOtpField.Primary(
                    onValueChange = { value = it },
                )
            }
        }
    }
}
