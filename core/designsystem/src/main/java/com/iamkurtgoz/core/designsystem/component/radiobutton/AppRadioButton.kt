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
package com.iamkurtgoz.core.designsystem.component.radiobutton

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.extension.noRippleClickable
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR

object AppRadioButton {
    @Composable
    fun Primary(
        selected: Boolean,
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        rightContent: (@Composable RowScope.() -> Unit)? = null,
        enabled: Boolean = true,
        colors: RadioButtonColors = RadioButtonDefaults.colors(
            selectedColor = AppTheme.colors.buttonColors.radioButtonPrimaryColors.radioButtonPrimarySelectedColor,
            unselectedColor = AppTheme.colors.buttonColors.radioButtonPrimaryColors.radioButtonPrimaryUnSelectedColor,
            disabledSelectedColor = AppTheme.colors.generalColors.foregroundDisabled,
            disabledUnselectedColor = AppTheme.colors.generalColors.foregroundDisabled,
        ),
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        onClick: () -> Unit = { },
    ) {
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            modifier = modifier
                .noRippleClickable {
                    onClick.invoke()
                },
        ) {
            leftContent?.invoke(this)
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides AppTheme.dimens.dp0) {
                RadioButton(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    colors = colors,
                    interactionSource = interactionSource,
                )
            }
            rightContent?.invoke(this)
        }
    }

    @Composable
    fun Secondary(
        selected: Boolean,
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
        leftContent: (@Composable RowScope.() -> Unit)? = null,
        rightContent: (@Composable RowScope.() -> Unit)? = null,
        colors: RadioButtonColors = RadioButtonDefaults.colors(
            selectedColor = AppTheme.colors.buttonColors.radioButtonSecondaryColors.radioButtonSecondarySelectedColor,
            unselectedColor = AppTheme.colors.buttonColors.radioButtonSecondaryColors.radioButtonSecondaryUnSelectedColor,
            disabledSelectedColor = AppTheme.colors.generalColors.foregroundDisabled,
            disabledUnselectedColor = AppTheme.colors.generalColors.foregroundDisabled,
        ),
        isClickable: Boolean = true,
        onClick: () -> Unit = { },
    ) {
        val transition = updateTransition(targetState = selected, label = "radioTransition")
        val backgroundColor by transition.animateColor(label = "bgColor") { isSelected ->
            if (isSelected) colors.selectedColor else Color.Transparent
        }
        val borderColor by transition.animateColor(label = "borderColor") { isSelected ->
            if (isSelected) colors.selectedColor else colors.unselectedColor
        }
        val checkScale by transition.animateFloat(label = "checkScale") { isSelected ->
            if (isSelected) AppDefaults.WEIGHT_FULL else AppDefaults.WEIGHT_NONE
        }

        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            modifier = modifier
                .noRippleClickable {
                    onClick.invoke()
                },
        ) {
            leftContent?.invoke(this)
            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.dp22)
                    .border(
                        width = AppTheme.dimens.dp2,
                        color = borderColor,
                        shape = AppTheme.shapes.radiusCircle,
                    )
                    .background(
                        color = backgroundColor,
                        shape = AppTheme.shapes.radiusCircle,
                    )
                    .clip(AppTheme.shapes.radiusCircle)
                    .clickable(onClick = onClick, enabled = isClickable),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_radio_button_selected_check),
                    contentDescription = null,
                    modifier = Modifier
                        .size(AppTheme.dimens.dp14)
                        .graphicsLayer {
                            scaleX = checkScale
                            scaleY = checkScale
                        },
                )
            }
            rightContent?.invoke(this)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppRadioButtonPreview() {
    var selected by remember { mutableStateOf(true) }
    AppTheme {
        Scaffold(
            contentColor = AppTheme.colors.generalColors.foregroundPrimary,
        ) {
            Column {
                Text("Primary")

                AppRadioButton.Primary(
                    selected = selected,
                ) {
                    selected = !selected
                }

                AppRadioButton.Primary(
                    selected = selected,
                    leftContent = {
                        Text(text = "Test")
                    },
                ) {
                    selected = !selected
                }

                AppRadioButton.Primary(
                    selected = selected,
                    enabled = false,
                    leftContent = {
                        Text(text = "Test")
                    },
                ) {
                    selected = !selected
                }

                AppRadioButton.Primary(
                    selected = selected,
                    rightContent = {
                        Text(text = "Test")
                    },
                ) {
                    selected = !selected
                }

                Text(
                    text = "Secondary",
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingHuge),
                )

                AppRadioButton.Secondary(
                    selected = selected,
                ) {
                    selected = !selected
                }
            }
        }
    }
}
