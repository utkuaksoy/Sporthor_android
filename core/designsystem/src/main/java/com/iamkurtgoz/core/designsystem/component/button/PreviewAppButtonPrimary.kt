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
package com.iamkurtgoz.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR

@PreviewAppWithNightMode
@Composable
private fun PreviewPrimary() {
    AppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.generalColors.backgroundPrimary)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text("Button Primary Large")
            }

            item {
                AppButton.PrimaryLarge(
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                    onClick = {},
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                )

                AppButton.PrimaryLarge(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Disabled",
                    enabled = false,
                    onClick = {},
                )
            }

            item {
                Text("Button Primary Medium")
            }

            item {
                AppButton.PrimaryMedium(
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                )

                AppButton.PrimaryMedium(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                    onClick = {},
                )

                AppButton.PrimaryMedium(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                )

                AppButton.PrimaryMedium(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Disabled",
                    enabled = false,
                    onClick = {},
                )
            }

            item {
                Text("Button Primary Small")
            }

            item {
                AppButton.PrimarySmall(
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                )

                AppButton.PrimarySmall(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                    onClick = {},
                )

                AppButton.PrimarySmall(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Enabled",
                    enabled = true,
                    onClick = {},
                    leftIcon = resourcesR.drawable.img_plus,
                    rightIcon = resourcesR.drawable.img_plus,
                )

                AppButton.PrimarySmall(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp10),
                    text = "Button Primary Disabled",
                    enabled = false,
                    onClick = {},
                )
            }
        }
    }
}
