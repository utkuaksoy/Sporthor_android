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
package com.iamkurtgoz.core.designsystem.component.textfield

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

object AppTextField {
    @Composable
    fun Primary(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        suggestions: List<String> = listOf(),
        title: String = "",
        placeholder: String? = null,
        hint: String = "",
        enabled: Boolean = true,
        fillMaxWidth: Boolean = true,
        colors: TextFieldColors = AppTextFieldColors.primaryColors(),
        sizes: TextFieldSizes = AppTextFieldSizes.primarySizes(),
        borders: TextFieldBorders = AppTextFieldBorders.primaryBorders(),
        shapes: TextFieldShapes = AppTextFieldShapes.primaryShapes(),
        styles: TextFieldStyles = AppTextFieldStyles.primaryStyles(),
        @DrawableRes leadingIcon: Int? = null,
        @DrawableRes trailingIcon: Int? = null,
        isError: Boolean = false,
        readOnly: Boolean = false,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        singleLine: Boolean = true,
        maxLines: Int = if (singleLine) AppDefaults.LINE_LIMIT_SINGLE else Int.MAX_VALUE,
        minLines: Int = AppDefaults.LINE_LIMIT_SINGLE,
        maxLength: Int = Int.MAX_VALUE,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        focusRequester: FocusRequester = remember { FocusRequester() },
        leadingIconClick: () -> Unit = {},
        trailingIconClick: () -> Unit = {},
    ) = AppTextFieldImpl(
        value = value,
        onValueChange = onValueChange,
        title = title,
        placeholder = placeholder,
        hint = hint,
        modifier = modifier,
        suggestions = suggestions,
        enabled = enabled,
        fillMaxWidth = fillMaxWidth,
        colors = colors,
        sizes = sizes,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        maxLength = maxLength,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        focusRequester = focusRequester,
        leadingIconClick = leadingIconClick,
        trailingIconClick = trailingIconClick,
    )

    @Composable
    fun SearchField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String? = null,
        fillMaxWidth: Boolean = true,
        colors: TextFieldColors = AppTextFieldColors.searchFieldColors(),
        sizes: TextFieldSizes = AppTextFieldSizes.searchFieldSizes(),
        borders: TextFieldBorders = AppTextFieldBorders.searchFieldBorders(),
        shapes: TextFieldShapes = AppTextFieldShapes.searchFieldShapes(),
        styles: TextFieldStyles = AppTextFieldStyles.searchFieldStyles(),
        @DrawableRes leadingIcon: Int? = null,
        @DrawableRes trailingIcon: Int? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        singleLine: Boolean = true,
        maxLines: Int = if (singleLine) AppDefaults.LINE_LIMIT_SINGLE else Int.MAX_VALUE,
        minLines: Int = AppDefaults.LINE_LIMIT_SINGLE,
        maxLength: Int = Int.MAX_VALUE,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        focusRequester: FocusRequester = remember { FocusRequester() },
        leadingIconClick: () -> Unit = {},
        trailingIconClick: () -> Unit = {},
    ) = AppTextFieldImpl(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        fillMaxWidth = fillMaxWidth,
        colors = colors,
        sizes = sizes,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        maxLength = maxLength,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        focusRequester = focusRequester,
        leadingIconClick = leadingIconClick,
        trailingIconClick = trailingIconClick,
    )

    @Composable
    fun MessageField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String? = null,
        fillMaxWidth: Boolean = true,
        colors: TextFieldColors = AppTextFieldColors.messageFieldColors(),
        sizes: TextFieldSizes = AppTextFieldSizes.messageFieldSizes(),
        borders: TextFieldBorders = AppTextFieldBorders.messageFieldBorders(),
        shapes: TextFieldShapes = AppTextFieldShapes.messageFieldShapes(),
        styles: TextFieldStyles = AppTextFieldStyles.messageFieldStyles(),
        @DrawableRes leadingIcon: Int? = null,
        @DrawableRes trailingIcon: Int? = null,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        singleLine: Boolean = true,
        maxLines: Int = if (singleLine) AppDefaults.LINE_LIMIT_SINGLE else Int.MAX_VALUE,
        minLines: Int = AppDefaults.LINE_LIMIT_SINGLE,
        maxLength: Int = Int.MAX_VALUE,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        focusRequester: FocusRequester = remember { FocusRequester() },
        leadingIconClick: () -> Unit = {},
        trailingIconClick: () -> Unit = {},
    ) = AppTextFieldImpl(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        fillMaxWidth = fillMaxWidth,
        colors = colors,
        sizes = sizes,
        borders = borders,
        shapes = shapes,
        styles = styles,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        maxLength = maxLength,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        focusRequester = focusRequester,
        leadingIconClick = leadingIconClick,
        trailingIconClick = trailingIconClick,
    )
}

@Composable
internal fun AppTextFieldImpl(
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    sizes: TextFieldSizes,
    borders: TextFieldBorders,
    shapes: TextFieldShapes,
    styles: TextFieldStyles,
    modifier: Modifier = Modifier,
    suggestions: List<String> = listOf(),
    title: String = "",
    placeholder: String? = null,
    hint: String = "",
    enabled: Boolean = true,
    fillMaxWidth: Boolean = true,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    isError: Boolean = false,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) AppDefaults.LINE_LIMIT_SINGLE else Int.MAX_VALUE,
    minLines: Int = AppDefaults.LINE_LIMIT_SINGLE,
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusRequester: FocusRequester = remember { FocusRequester() },
    leadingIconClick: () -> Unit = {},
    trailingIconClick: () -> Unit = {},
) {
    val showAutoFillDialog = remember(value, suggestions) {
        !suggestions.any { it == value }
    }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderModifier by borders.borderModifier(enabled, isError, interactionSource)
    val titleColor by colors.titleColor(enabled, isError, interactionSource)
    val containerColor by colors.containerColor(enabled, isError, interactionSource)
    val contentColor by colors.contentColor(enabled, isError, interactionSource)
    val leadingIconColor by colors.leadingIconColor(enabled)
    val trailingColor by colors.trailingIconColor(enabled)
    val hintContentColor by colors.hintColor(enabled, isError)
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = value)
        }
    }
    
    Column(
        modifier = modifier,
    ) {
        AnimatedVisibility(visible = title.isNotEmpty()) {
            BasicText(
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.spacingSmall),
                text = title,
                style = styles.titleTextStyle,
                color = { titleColor },
                maxLines = AppDefaults.LINE_LIMIT_SINGLE,
                overflow = TextOverflow.Ellipsis,
            )
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                if (it.text.length <= maxLength) {
                    textFieldValue = it
                    onValueChange(it.text)
                }
            },
            modifier = Modifier
                .focusRequester(focusRequester),
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            textStyle = styles.contentTextStyle.copy(
                color = contentColor,
            ),
            cursorBrush = SolidColor(colors.cursorColor),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .height(sizes.height)
                        .background(
                            color = containerColor,
                            shape = shapes.roundedCornerShape,
                        )
                        .then(borderModifier)
                        .padding(sizes.contentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    leadingIcon?.let {
                        IconButton(
                            modifier = Modifier
                                .size(sizes.trailingIconSize),
                            onClick = leadingIconClick,
                            content = {
                                Image(
                                    modifier = Modifier
                                        .size(sizes.trailingIconSize),
                                    painter = painterResource(id = leadingIcon),
                                    contentDescription = "leading icon",
                                    colorFilter = ColorFilter.tint(leadingIconColor),
                                )
                            },
                        )

                        Spacer(
                            modifier = Modifier
                                .width(AppTheme.dimens.dp8),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .ifTrue(fillMaxWidth) {
                                this.weight(AppDefaults.WEIGHT_FULL)
                            },
                    ) {
                        if (value.isEmpty()) {
                            placeholder?.let {
                                Text(
                                    text = placeholder,
                                    style = styles.placeholderTextStyle,
                                    color = colors.placeholderColor,
                                )
                            }
                        }
                        innerTextField()
                    }

                    trailingIcon?.let {
                        Spacer(
                            modifier = Modifier
                                .width(AppTheme.dimens.dp8),
                        )

                        IconButton(
                            modifier = Modifier
                                .size(sizes.trailingIconSize),
                            onClick = trailingIconClick,
                            content = {
                                Image(
                                    modifier = Modifier
                                        .size(sizes.trailingIconSize),
                                    painter = painterResource(id = trailingIcon),
                                    contentDescription = "trailing icon",
                                    colorFilter = if (trailingColor.isUnspecified) null else ColorFilter.tint(trailingColor),
                                )
                            },
                        )
                    }
                }
            },
        )

        AnimatedVisibility(visible = showAutoFillDialog && isFocused && suggestions.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.spacingSmallest)
                    .heightIn(max = 240.dp),
                shape = shapes.roundedCornerShape,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
            ) {
                LazyColumn {
                    itemsIndexed(
                        items = suggestions,
                        key = { index, item -> index.toString() + item },
                        itemContent = { index, item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        textFieldValue = textFieldValue.copy(
                                            text = item,
                                            selection = TextRange(item.length)
                                        )
                                        onValueChange(item)
                                    }
                                    .padding(
                                        horizontal = AppTheme.spacing.spacingMedium,
                                        vertical = AppTheme.spacing.spacingSmall,
                                    ),
                            ) {
                                Text(
                                    text = item,
                                    style = styles.contentTextStyle,
                                    color = colors.contentColor(enabled = true, isError = false, interactionSource = interactionSource).value,
                                )
                            }
                            HorizontalDivider()
                        },
                    )
                }
            }
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
@PreviewAppWithNightMode
private fun Preview() {
    var canShowPassword by remember { mutableStateOf(false) }
    var value: String by remember { mutableStateOf("") }
    AppTheme {
        AppThemeScaffold {
            Column(
                modifier = Modifier
                    .padding(all = AppTheme.spacing.spacingMedium),
            ) {
                AppTextField.Primary(
                    title = "Kullanıcı adı",
                    value = value,
                    suggestions = listOf(
                        "iamkurtgoz",
                        "test",
                    ),
                    onValueChange = { value = it },
                )

                AppTextField.Primary(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium),
                    title = "Kullanıcı adı",
                    placeholder = "Kullanıcı Adını Gir",
                    value = value,
                    onValueChange = { value = it },
                    isError = true,
                )

                AppTextField.Primary(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium),
                    title = "Kullanıcı adı",
                    placeholder = "Kullanıcı Adını Gir",
                    hint = "Hint",
                    value = value,
                    onValueChange = { value = it },
                    isError = true,
                )

                AppTextField.Primary(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium),
                    title = "Şifre",
                    placeholder = "Şifreni Gir",
                    value = value,
                    onValueChange = { value = it },
                    isError = true,
                    trailingIcon = resourcesR.drawable.img_eye_slash,
                    trailingIconClick = {
                        canShowPassword = !canShowPassword
                    },
                    visualTransformation = if (canShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                )

                AppTextField.SearchField(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium),
                    placeholder = "Arama Yap",
                    value = value,
                    onValueChange = { value = it },
                    trailingIcon = if (value.isNotEmpty()) resourcesR.drawable.img_close_circle else null,
                    leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                )

                AppTextField.MessageField(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium),
                    placeholder = "Gönder",
                    value = value,
                    onValueChange = { value = it },
                    trailingIcon = resourcesR.drawable.img_chat_message_send_disabled,
                )
            }
        }
    }
}
