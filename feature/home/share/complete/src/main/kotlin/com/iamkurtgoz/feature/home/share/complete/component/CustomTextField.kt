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
package com.iamkurtgoz.feature.home.share.complete.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.navigation.HomeScreenShareCompleteRoute
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModelMediaItem
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteShareTypeScreenNavigateModel
import com.iamkurtgoz.core.resources.R
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.feature.home.share.complete.CompleteScreenContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun CustomTextField(
    state: CompleteScreenContract.State,
    setEvent: (CompleteScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.spacing.spacingMedium)
                .height(AppTheme.dimens.dp256)
                .bringIntoViewRequester(bringIntoViewRequester)
                .focusTarget()
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            value = state.textContent.value,
            onValueChange = {
                val event = CompleteScreenContract.Event.SetTextContent(
                    value = it,
                )
                setEvent(event)
                coroutineScope.launch {
                    delay(CompleteScreenContract.Static.DELAY_100_MILLISECOND)
                    bringIntoViewRequester.bringIntoView()
                }
            },
            textStyle = AppTheme.typography.bodyLarge.copy(
                color = AppTheme.colors.generalColors.textWhite,
            ),
            cursorBrush = SolidColor(AppTheme.colors.generalColors.textWhiteSecondary),
            decorationBox = { innerTextField ->
                if (state.textContent.isEmpty) {
                    Text(
                        text = "Açıklama Ekle", // TODO: Localize
                        style = AppTheme.typography.bodyLarge,
                        color = AppTheme.colors.generalColors.textWhiteSecondary,
                    )
                }
                innerTextField.invoke()
            },
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium),
            color = AppTheme.colors.generalColors.foregroundSecondary,
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.generalColors.foregroundPrimary)
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(R.drawable.img_share_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }

        CustomTextField(
            state = CompleteScreenContract.State(
                isLoading = true,
                appBuildConfigStatePack = AppBuildConfigStatePack(),
                appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                navigateRoute = HomeScreenShareCompleteRoute(
                    routeType = HomeScreenShareCompleteScreenNavigateModel(
                        shareType = HomeScreenShareCompleteShareTypeScreenNavigateModel.CreateStory,
                        selectedMediaList = listOf(
                            HomeScreenShareCompleteScreenNavigateModelMediaItem(
                                customMediaType = CustomMediaType.IMAGE,
                                uri = Uri.EMPTY,
                            ),
                        ),
                    ),
                ),
            ),
            setEvent = { },
        )
    }
}
