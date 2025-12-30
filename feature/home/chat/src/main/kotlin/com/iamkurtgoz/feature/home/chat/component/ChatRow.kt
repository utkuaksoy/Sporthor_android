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
package com.iamkurtgoz.feature.home.chat.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.DateFormat
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.extensions.toString
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.chip.AppChip
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.domain.model.base.BasicAppChipItem
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.feature.home.chat.ChatScreenContract
import com.iamkurtgoz.feature.home.chat.domain.mock.MockData
import com.iamkurtgoz.feature.home.chat.domain.model.ChatAllMessageItemUIModel

@Composable
internal fun ChatRow(
    item: ChatAllMessageItemUIModel?,
    modifier: Modifier = Modifier,
    setEvent: (ChatScreenContract.Event) -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(durationMillis = 300),
        label = "swipeOffset",
    )

    val maxSwipe = -120f
    val swipeThreshold = -60f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    setEvent.invoke(ChatScreenContract.Event.HideMessage(item?.userId))
                    setEvent.invoke(ChatScreenContract.Event.FetchChatList(FetchParam.RELOAD))
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Sil",
                    tint = Color.White,
                )
            }
        }

        Column(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.toInt(), 0) }
                .zIndex(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // Arka planı kapatmak için
                .clickable {
                    val event = ChatScreenContract.Event.NavigateToMessagingScreen(
                        isGroup = item?.isGroup == true,
                        title = item?.name ?: "",
                        channelId = item?.userId ?: "",
                        userId = item?.toUserId ?: "",
                    )
                    setEvent(event)
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffset = offsetX + dragAmount
                            offsetX = newOffset.coerceIn(maxSwipe, 0f)
                        },
                        onDragEnd = {
                            offsetX = if (offsetX > swipeThreshold) 0f else maxSwipe
                        },
                    )
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserImageView(
                    modifier = Modifier
                        .padding(top = AppTheme.dimens.dp12),
                    data = item?.image ?: item?.name?.getUserNameFirstChar(),
                    hasBorder = false,
                    badgeData = null,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = AppTheme.spacing.spacingSmallest)
                        .padding(top = AppTheme.spacing.spacingMedium),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = item?.name ?: "",
                            style = AppTheme.typography.labelMedium,
                            modifier = Modifier.weight(AppDefaults.WEIGHT_FULL),
                        )

                        Text(
                            text = item?.messageDateLong?.toString(format = DateFormat.TIME) ?: "",
                            style = AppTheme.typography.labelRegular,
                            color = AppTheme.colors.generalColors.textSecondary,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.dimens.dp2),
                    ) {
                        Text(
                            text = item?.lastMessage ?: "",
                            style = AppTheme.typography.labelRegular,
                            color = AppTheme.colors.generalColors.textSecondary,
                            modifier = Modifier.weight(AppDefaults.WEIGHT_FULL),
                            minLines = AppDefaults.LINE_LIMIT_DOUBLE,
                            maxLines = AppDefaults.LINE_LIMIT_DOUBLE,
                        )

                        item?.unReadMessageCount?.takeIf { it > 0 }?.let { unReadMessageCount ->
                            AppChip.Secondary(
                                item = BasicAppChipItem(
                                    title = unReadMessageCount.toString(),
                                ),
                                isSelected = true,
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = AppTheme.dimens.dp12),
            )
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            LazyColumn {
                itemsIndexed(
                    items = MockData.messagesList,
                    key = { index, item ->
                        "${item.uuid}-$index"
                    },
                    itemContent = { index, item ->
                        ChatRow(
                            item = item,
                            setEvent = { },
                        )
                    },
                )
            }
        }
    }
}
