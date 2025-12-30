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
package com.iamkurtgoz.core.designsystem.component.toolbar

import android.R as androidR
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import com.iamkurtgoz.core.designsystem.theme.AppTheme

object AppToolbar {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Toolbar(
        modifier: Modifier = Modifier,
        topAppBarState: TopAppBarState = rememberTopAppBarState(),
        leftContent: (@Composable AppToolbar.() -> Unit)? = null,
        centerContent: (@Composable AppToolbar.() -> Unit)? = null,
        rightContent: (@Composable RowScope.() -> Unit)? = null,
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

        CenterAlignedTopAppBar(
            modifier = modifier
                .shadow(elevation = AppTheme.dimens.dp8),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.generalColors.backgroundPrimary,
                titleContentColor = AppTheme.colors.generalColors.textPrimary,
            ),
            title = {
                centerContent?.invoke(this)
            },
            navigationIcon = {
                leftContent?.invoke(this)
            },
            actions = {
                rightContent?.invoke(this)
            },
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppToolbarPreview() {
    var subText by remember { mutableStateOf("") }
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentColor = AppTheme.colors.generalColors.foregroundPrimary,
            topBar = {
                AppToolbar.Toolbar(
                    leftContent = {
                        AppToolbarFields.NavigateIcon {
                        }
                    },
                    centerContent = {
                        AppToolbarFields.Title(text = subText)
                    },
                    rightContent = {
                        AppToolbarFields.ImageIcon(resId = androidR.drawable.ic_menu_revert) { }
                        AppToolbarFields.ImageIcon(resId = androidR.drawable.ic_menu_add) { }
                    },
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                AppToolbar.Toolbar(
                    centerContent = {
                        AppToolbarFields.Title(text = "App Toolbar")
                    },
                    rightContent = {
                        AppToolbarFields.ImageIcon(resId = androidR.drawable.ic_menu_revert) {
                            subText = if (subText.isEmpty()) {
                                "çevrimiçi"
                            } else {
                                ""
                            }
                        }
                    },
                )

                AppToolbar.Toolbar(
                    centerContent = {
                        AppToolbarFields.TitleWithSubTitle(
                            text = "Mehmet",
                            subText = subText,
                        )
                    },
                    rightContent = {
                        AppToolbarFields.ImageIcon(resId = androidR.drawable.ic_menu_revert) {
                            subText = if (subText.isEmpty()) {
                                "çevrimiçi"
                            } else {
                                ""
                            }
                        }
                    },
                )

                var searchQuery by remember { mutableStateOf("default query") }
                var isSearchActive by remember { mutableStateOf(false) }
                AppToolbar.Toolbar(
                    leftContent = {
                        AppToolbarFields.NavigateIcon {
                            if (isSearchActive) {
                                isSearchActive = false
                            }
                        }
                    },
                    centerContent = {
                        if (isSearchActive) {
                            TextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                },
                            )
                            /*
                            AppTextFields.Outlined(
                                value = searchQuery,
                                sizes = AppTextFieldSizesDefaults.small(),
                                onValueChange = {
                                    searchQuery = it
                                },
                            )
                             */
                        } else {
                            AppToolbarFields.Title(text = "App Toolbar")
                        }
                    },
                    rightContent = {
                        if (!isSearchActive) {
                            AppToolbarFields.ImageIcon(resId = androidR.drawable.ic_menu_search) {
                                isSearchActive = true
                            }
                        }
                    },
                )
            }
        }
    }
}
