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
package com.iamkurtgoz.core.commonui.component.successClubCreateBottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessClubCreateBottomSheet(
    isShow: Boolean,
    imageData: Any?,
    clubName: String,
    isEdit: Boolean = false,
    onSendDocumentClick: () -> Unit,
    onSkipThisPartClick: () -> Unit,
) {
    if (isShow) {
        val sheetState: SheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            },
        )
        ModalBottomSheet(
            containerColor = AppTheme.colors.generalColors.backgroundPrimary,
            scrimColor = AppTheme.colors.generalColors.backgroundPrimary.copy(
                alpha = AppDefaults.COMPOSE_COLORS_THREE_QUARTER_ALPHA,
            ),
            sheetState = sheetState,
            onDismissRequest = { },
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false,
            ),
            content = {
                SuccessClubCreateBottomSheetContent(
                    imageData = imageData,
                    clubName = clubName,
                    onSendDocumentClick = onSendDocumentClick,
                    onSkipThisPartClick = onSkipThisPartClick,
                    isEdit = isEdit,
                )
            },
        )
    }
}

@Composable
private fun SuccessClubCreateBottomSheetContent(
    imageData: Any?,
    clubName: String,
    onSendDocumentClick: () -> Unit,
    onSkipThisPartClick: () -> Unit,
    isEdit: Boolean,
    modifier: Modifier = Modifier,
) {
    val density: Density = LocalDensity.current
    var dynamicConfettiHeight by remember { mutableStateOf(AppDefaults.ZERO.dp) }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        content = {
            Image(
                painter = painterResource(resourcesR.drawable.img_confetti),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        dynamicConfettiHeight = with(density) {
                            it.size.height.toDp()
                        }
                    },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dynamicConfettiHeight / AppDefaults.FIVE)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .clip(AppTheme.shapes.radiusCircle)
                        .size(AppTheme.dimens.dp72)
                        .border(
                            width = AppTheme.dimens.dp2,
                            color = AppTheme.colors.generalColors.foregroundDisabled,
                            shape = AppTheme.shapes.radiusCircle,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    AppAsyncImageLoader.Load(
                        modifier = Modifier
                            .clip(AppTheme.shapes.radiusCircle)
                            .size(AppTheme.dimens.dp68),
                        data = imageData,
                    )
                }

                Text(
                    text = clubName,
                    style = AppTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = if (isEdit) "Tebrikler! Kulübünü Güncelledin." else "Tebrikler! Kulübünü Oluşturdun.", // TODO: Localize
                    style = AppTheme.typography.heading05,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingLarge),
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "Şimdi antrenörlerini ve teknik ekibini davet et ve güçlü bir topluluk oluşturmaya başla.", // TODO: Localize
                    style = AppTheme.typography.bodyMediumCompact,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingLarge),
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "Birlikte başarıya ulaşmak için takım ruhunu yakala! \uD83D\uDE80", // TODO: Localize
                    style = AppTheme.typography.bodyMediumCompact,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingLarge),
                    textAlign = TextAlign.Center,
                )

                AppButton.PrimaryLarge(
                    text = "Yetki Belgesi Gönder", // TODO: Localize
                    onClick = onSendDocumentClick,
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp88)
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                )

                AppButton.OutlineLarge(
                    text = "Bu Adımı Atla", // TODO: Localize
                    onClick = onSkipThisPartClick,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular)
                        .padding(bottom = AppTheme.spacing.spacingHuge)
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                )
            }
        },
    )
}

private class EditStatusPreviewParameter: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Composable
@PreviewAppWithNightMode
private fun SuccessClubCreateBottomSheetPreview(
    @PreviewParameter(EditStatusPreviewParameter::class) isEdit: Boolean,
) {
    AppTheme {
        AppThemeScaffold {
            SuccessClubCreateBottomSheetContent(
                imageData = resourcesR.drawable.temp_team_image_ezcacibasi,
                clubName = "Esen Spor Kulübü",
                onSendDocumentClick = {},
                onSkipThisPartClick = {},
                isEdit = isEdit,
            )
        }
    }
}
