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
package com.iamkurtgoz.feature.home.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.feature.home.profile.ProfileScreenContract
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileActionButtonComponentType
import com.iamkurtgoz.feature.home.profile.domain.types.toProfileActionButtonType

@Composable
internal fun ProfileActionButtons(
    userId: String,
    buttons: List<String>,
    setEvent: (ProfileScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.spacingMedium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        userScrollEnabled = false,
    ) {
        itemsIndexed(
            items = buttons,
            key = { index, item ->
                "$index-$item"
            },
            itemContent = { index, item ->
                if (item.toProfileActionButtonType().buttonType == ProfileActionButtonComponentType.Primary) {
                    AppButton.PrimaryMedium(
                        text = item.toProfileActionButtonType().title,
                        onClick = {
                            setEvent.invoke(ProfileScreenContract.Event.OnClickActionButton(item.toProfileActionButtonType(), userId))
                        },
                        modifier = Modifier
                            .toButtonModifier(index, buttons.size),
                    )
                } else if (item.toProfileActionButtonType().buttonType == ProfileActionButtonComponentType.Outline) {
                    AppButton.OutlineMedium(
                        text = item.toProfileActionButtonType().title,
                        onClick = {
                            setEvent.invoke(ProfileScreenContract.Event.OnClickActionButton(item.toProfileActionButtonType(), userId))
                        },
                        modifier = Modifier
                            .toButtonModifier(index, buttons.size),
                    )
                }
            },
        )
    }
}

@Suppress("CyclomaticComplexMethod")
@Composable
internal fun Modifier.toButtonModifier(
    index: Int,
    buttonSize: Int,
): Modifier {
    val spacing = AppTheme.spacing.spacingSmallest
    val totalSpacing = when (buttonSize) {
        AppDefaults.ONE -> spacing * AppDefaults.TWO
        else -> spacing * (buttonSize - AppDefaults.ONE)
    }
    val buttonWidth = (AppTheme.configuration.getScreenWidthDp() - AppTheme.spacing.spacingMedium - totalSpacing) / buttonSize
    return this
        .width(buttonWidth)
        .let { modifier ->
            when (buttonSize) {
                AppDefaults.TWO -> when (index) {
                    AppDefaults.ZERO -> modifier.padding(end = spacing)
                    AppDefaults.ONE -> modifier.padding(start = spacing)
                    else -> modifier
                }
                AppDefaults.THREE -> when (index) {
                    AppDefaults.ZERO -> modifier.padding(end = spacing)
                    AppDefaults.ONE -> modifier.padding(horizontal = spacing)
                    AppDefaults.TWO -> modifier.padding(start = spacing)
                    else -> modifier
                }
                AppDefaults.FOUR -> when (index) {
                    AppDefaults.ZERO -> modifier.padding(end = spacing)
                    AppDefaults.ONE, AppDefaults.TWO -> modifier.padding(horizontal = spacing)
                    AppDefaults.THREE -> modifier.padding(start = spacing)
                    else -> modifier
                }
                else -> modifier
            }
        }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Column {
                ProfileActionButtons(
                    userId = "d42342ıcj2i34j2",
                    buttons = listOf(
                        "editProfile",
                    ),
                    setEvent = {},
                )

                ProfileActionButtons(
                    userId = "d42342ıcj2i34j2",
                    buttons = listOf(
                        "follow",
                        "message",
                    ),
                    setEvent = {},
                )

                ProfileActionButtons(
                    userId = "d42342ıcj2i34j2",
                    buttons = listOf(
                        "follow",
                        "message",
                        "message",
                    ),
                    setEvent = {},
                )

                ProfileActionButtons(
                    userId = "d42342ıcj2i34j2",
                    buttons = listOf(
                        "follow",
                        "message",
                        "follow",
                        "message",
                    ),
                    setEvent = {},
                )
            }
        }
    }
}
