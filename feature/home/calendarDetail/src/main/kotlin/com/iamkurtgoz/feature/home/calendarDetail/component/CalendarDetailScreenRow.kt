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
package com.iamkurtgoz.feature.home.calendarDetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelTask
import com.iamkurtgoz.feature.home.calendarDetail.domain.model.mockCalendarDetailEvent
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun CalendarDetailScreenRow(
    item: CalendarDetailEventUIModelTask,
    modifier: Modifier = Modifier,
    onClick: (CalendarDetailEventUIModelTask) -> Unit = {},
    onRPEClick: (CalendarDetailEventUIModelTask) -> Unit = {},
    onMapClick: (com.iamkurtgoz.feature.home.calendarDetail.domain.model.CalendarDetailEventUIModelLocation) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = AppTheme.shapes.radiusMedium,
        color = AppTheme.colors.generalColors.backgroundWeak100,
        shadowElevation = AppTheme.dimens.dp0dot5,
        border = BorderStroke(
            width = AppTheme.dimens.dp1,
            color = AppTheme.colors.generalColors.borderSoft200,
        ),
    ) {
        Column(
            modifier = Modifier
                .clickable(enabled = item.isOwn == true) {
                    onClick.invoke(item)
                }
                .fillMaxWidth()
                .padding(all = AppTheme.spacing.spacingMedium),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.title ?: "-",
                    style = AppTheme.typography.heading06,
                )

                Spacer(modifier = Modifier.weight(1f))

                item.taskType?.let { taskType ->
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(AppTheme.shapes.radiusCircle)
                            .background(Color(taskType.color)),
                    )

                    Text(
                        text = taskType.name ?: "-",
                        style = AppTheme.typography.subtitleSmall,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingSmallest),
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium),
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_time),
                    contentDescription = null,
                )

                Text(
                    text = item.hour ?: "-",
                    style = AppTheme.typography.subtitleSmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = AppTheme.colors.generalColors.contentSoft600,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmallest),
                )
            }

            item.description?.let { description ->
                Text(
                    text = description,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.generalColors.contentSoft600,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    item.trainingGroup?.let { trainingGroup ->
                        Row(
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingRegular),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AppAsyncImageLoader.Load(
                                data = trainingGroup.detail,
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp24)
                                    .background(
                                        color = AppTheme.colors.generalColors.backgroundSoft200,
                                        shape = AppTheme.shapes.radiusCircle,
                                    )
                                    .clip(AppTheme.shapes.radiusCircle),
                            )

                            Text(
                                text = trainingGroup.name ?: "",
                                style = AppTheme.typography.subtitleSmall,
                                modifier = Modifier
                                    .padding(start = AppTheme.spacing.spacingRegular),
                            )
                        }
                    }

                    item.users?.let { users ->
                        Box(
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingRegular),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            users.fastForEachIndexed { index, item ->
                                key(item) {
                                    Box(
                                        modifier = Modifier
                                            .padding(start = (index * 12).dp)
                                            .size(if (index == AppDefaults.ZERO) AppTheme.dimens.dp18 else AppTheme.dimens.dp24)
                                            .background(
                                                color = AppTheme.colors.generalColors.backgroundWeak100,
                                                shape = AppTheme.shapes.radiusCircle,
                                            )
                                            .clip(AppTheme.shapes.radiusCircle),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        AppAsyncImageLoader.Load(
                                            data = item?.imageUrl ?: item?.name.getUserNameFirstChar(),
                                            modifier = Modifier
                                                .size(AppTheme.dimens.dp18)
                                                .background(
                                                    color = AppTheme.colors.generalColors.backgroundSoft200,
                                                    shape = AppTheme.shapes.radiusCircle,
                                                )
                                                .clip(AppTheme.shapes.radiusCircle),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (item.isOwn == true) {
                    Image(
                        painter = painterResource(resourcesR.drawable.img_arrow_right),
                        contentDescription = null,
                    )
                }

                if (item.taskType?.name.equals("antreman", ignoreCase = true) ||
                    item.taskType?.name.equals("müsabaka", ignoreCase = true)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingRegular)
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                            .height(28.dp),
                        onClick = { onRPEClick(item) },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        border = BorderStroke(1.dp, AppTheme.colors.generalColors.textSecondary),
                    ) {
                        Text(
                            text = "RPE Puanla",
                            style = AppTheme.typography.bodyMedium.copy(
                                color = AppTheme.colors.generalColors.textSecondary,
                                fontSize = 12.sp,
                            ),
                        )
                    }
                }
            }

            item.location?.let { location ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .clickable { onMapClick(location) },
                ) {
                    Image(
                        painter = painterResource(resourcesR.drawable.img_location_pin),
                        contentDescription = null,
                    )

                    Text(
                        text = "Haritada Göster",
                        style = AppTheme.typography.subtitleSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingSmallest),
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(resourcesR.drawable.img_arrow_right),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun RPEBottomSheet(
    selectedRate: Int,
    onRateChange: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onDismiss: () -> Unit,
    currentTask: CalendarDetailEventUIModelTask? = null,
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(resourcesR.drawable.img_back_arrow),
                        contentDescription = "Geri",
                        tint = Color.Black,
                    )
                }

                Text(
                    text = "RPE Puanla",
                    style = AppTheme.typography.heading06.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.width(48.dp)) // IconButton'ın boyutunu dengelemek için
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Puan ve Açıklama Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "Puan",
                    style = AppTheme.typography.subtitleSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Black,
                    modifier = Modifier.width(40.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "RPE Açıklama",
                    style = AppTheme.typography.subtitleSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RPE Descriptions
            RPEDescriptions()

            Spacer(modifier = Modifier.height(32.dp))

            // Star Rating
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                (1..10).forEach { index ->
                    Icon(
                        painter = painterResource(resourcesR.drawable.img_time),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onRateChange(index) },
                        tint = if (index <= selectedRate) Color(0xFF7ED957) else Color(0xFFE0E0E0),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7ED957),
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = "Kaydet",
                    style = AppTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}

@Composable
private fun RPEDescriptions() {
    val rpeList = listOf(
        "1" to "Çok Hafif Aktivite\nDinlenme hariç her şey",
        "2-3" to "Hafif Aktivite\nSaatlerce devam edebilirsiniz, nefes almak ve konuşmak kolay",
        "4-5" to "Orta Aktivite\nUzun süre devam edebilirsiniz, konuşmak ve kısa diyaloglar kolay",
        "6-7" to "Yoğun Aktivite\nZorlanmaya başladığınız an, nefes nefese ve belki bir cümle konuşabilirsiniz",
        "8-9" to "Çok Yoğun Aktivite\nEgzersize devam etmek veya tek kelimeden fazla konuşmak zor",
        "10" to "Maksimum Efor\nDevam etmek veya tek bir kelime konuşmak imkansız. Tamamen nefes nefese",
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        rpeList.forEach { (score, desc) ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = score,
                    style = AppTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.Black,
                    modifier = Modifier.width(40.dp),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    val lines = desc.split("\n")
                    Text(
                        text = lines[0],
                        style = AppTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                        color = Color.Black,
                    )
                    if (lines.size > 1) {
                        Text(
                            text = lines[1],
                            style = AppTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                        )
                    }
                }
            }

            HorizontalDivider()
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                itemsIndexed(
                    items = mockCalendarDetailEvent.tasks?.filterNotNull() ?: listOf(),
                    key = { index, item ->
                        "$index${item.id}"
                    },
                    itemContent = { index, item ->
                        CalendarDetailScreenRow(
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingMedium)
                                .padding(horizontal = AppTheme.spacing.spacingMedium),
                            item = item,
                        )
                    },
                )
            }
        }
    }
}
