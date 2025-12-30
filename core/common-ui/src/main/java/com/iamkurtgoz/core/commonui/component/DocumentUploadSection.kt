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
package com.iamkurtgoz.core.commonui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.util.fastForEachIndexed
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
fun DocumentUploadSection(
    documentList: List<DocumentUploadSectionItem>,
    onSelectDocumentClick: (index: Int) -> Unit,
    onAddNewDocumentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.spacing.spacingHuge,
                vertical = AppTheme.spacing.spacingMedium,
            ),
    ) {
        Text(
            text = "Kulüpte yetkili olduğunu belgeleyen dokümanları yükle",
            style = AppTheme.typography.labelMedium,
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium))

        documentList.fastForEachIndexed { index, item ->
            key(item.file) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .ifTrue(index > AppDefaults.ZERO) {
                            padding(top = AppTheme.spacing.spacingRegular)
                        },
                    shape = RoundedCornerShape(AppTheme.dimens.dp16),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colors.generalColors.backgroundWeak100,
                    ),
                    onClick = {
                        if (item.file == null) {
                            onSelectDocumentClick.invoke(index)   
                        }
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = AppTheme.spacing.spacingMedium),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(AppTheme.dimens.dp64)
                                .clip(RoundedCornerShape(AppTheme.dimens.dp16))
                                .background(AppTheme.colors.generalColors.primitivesPink500),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = resourcesR.drawable.img_file_upload),
                                contentDescription = "Yetki belgesi simgesi",
                                tint = AppTheme.colors.generalColors.foregroundWhite,
                                modifier = Modifier.size(AppTheme.dimens.dp32),
                            )
                        }

                        Spacer(modifier = Modifier.width(AppTheme.spacing.spacingMedium))

                        item.file?.split("/")?.last()?.let { 
                            Text(
                                text = it,
                                style = AppTheme.typography.labelMedium,
                            )
                        } ?: run {
                            Text(
                                text = "Yetki belgesi yükle",
                                style = AppTheme.typography.labelMedium,
                            )   
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .size(AppTheme.dimens.dp40)
                                .clip(CircleShape)
                                .background(AppTheme.colors.generalColors.foregroundBlack),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Yükle",
                                tint = AppTheme.colors.generalColors.foregroundWhite,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium))

        AppButton.OutlineLarge(
            text = "Yeni Belge Ekle", // TODO: Localize
            onClick = onAddNewDocumentClick,
            leftIcon = resourcesR.drawable.img_plus,
            modifier = Modifier,
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.dp20),
            )
            Spacer(modifier = Modifier.width(AppTheme.spacing.spacingSmall))
            Text(
                text = "PDF ya da word uzantılarını yükleyebilirsin",
                style = AppTheme.typography.bodyLarge,
            )
        }
    }
}

data class DocumentUploadSectionItem(
    val index: Int,
    val file: String?,
    val extension: String? = null,
)

@PreviewAppWithNightMode
@Composable
private fun DocumentUploadSectionPreview() {
    AppTheme {
        AppThemeSurface {
            DocumentUploadSection(
                documentList = listOf(
                    DocumentUploadSectionItem(
                        index = AppDefaults.ZERO,
                        file = null,
                    ),
                    DocumentUploadSectionItem(
                        index = AppDefaults.ONE,
                        file = null,
                    ),
                ),
                onSelectDocumentClick = {},
                onAddNewDocumentClick = {},
            )
        }
    }
}
