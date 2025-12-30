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
package com.iamkurtgoz.core.commonui.component.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
fun UserRow(
    userHeaderData: Any?,
    isHeaderUser: Boolean,
    title: String?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = AppTheme.spacing.spacingNone),
    headerSize: Dp = AppTheme.dimens.dp48,
    subTitle: Array<String>? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    onClickAction: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClickAction)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (isHeaderUser) {
            UserImageView(
                data = userHeaderData,
                size = headerSize,
            )
        } else {
            Box(
                modifier = Modifier
                    .size(headerSize + AppTheme.dimens.dp8)
                    .clip(AppTheme.shapes.radiusCircle),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(headerSize)
                        .border(
                            border = BorderStroke(
                                width = AppTheme.dimens.dp1,
                                color = AppTheme.colors.generalColors.borderSoft200,
                            ),
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .background(
                            color = AppTheme.colors.generalColors.transparent,
                            shape = AppTheme.shapes.radiusCircle,
                        )
                        .clip(AppTheme.shapes.radiusCircle),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(resourcesR.drawable.img_search),
                        contentDescription = "search",
                        modifier = Modifier
                            .size(AppTheme.dimens.dp18),
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(AppDefaults.WEIGHT_FULL)
                .padding(start = AppTheme.spacing.spacingSmall),
        ) {
            title?.let {
                Text(
                    text = title,
                    style = AppTheme.typography.subtitleSmall,
                )
            }

            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                subTitle?.let { subTitleList ->
                    itemsIndexed(
                        items = subTitleList,
                        key = { index, item ->
                            "$index$item"
                        },
                        itemContent = { index, item ->
                            Text(
                                text = item,
                                style = AppTheme.typography.helperText,
                                color = AppTheme.colors.generalColors.textSecondary,
                            )

                            if (index != subTitleList.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = AppTheme.spacing.spacingSmallest),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = AppTheme.colors.generalColors.contentSoft600,
                                                shape = AppTheme.shapes.radiusCircle,
                                            )
                                            .size(AppTheme.dimens.dp2),
                                    )
                                }
                            }
                        },
                    )
                }
            }
        }

        trailingContent?.invoke(this)
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            Column {
                UserRow(
                    userHeaderData = resourcesR.drawable.temp_img_profile_women,
                    isHeaderUser = true,
                    title = "Hakan Yılmaz",
                    subTitle = arrayOf("Baş Antrenör", "2000 Takipçi"),
                    trailingContent = {
                        UserRowFields.CloseIcon()
                    },
                )

                UserRow(
                    userHeaderData = "Mehmet Kurtgöz".getUserNameFirstChar(),
                    isHeaderUser = true,
                    title = "Mehmet Kurtgöz",
                    subTitle = arrayOf("1408 Takipçi"),
                    trailingContent = {
                        UserRowFields.CloseIcon()
                    },
                )

                UserRow(
                    userHeaderData = null,
                    isHeaderUser = false,
                    title = "tanerkoca",
                    trailingContent = {
                        UserRowFields.CloseIcon()
                    },
                )
            }
        }
    }
}
