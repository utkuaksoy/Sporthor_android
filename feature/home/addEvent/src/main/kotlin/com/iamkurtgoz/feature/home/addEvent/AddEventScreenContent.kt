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
package com.iamkurtgoz.feature.home.addEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.model.AppTextFieldValue
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenAddEventRoute
import com.iamkurtgoz.core.navigation.model.home.addEvent.HomeScreenAddEventScreenNavigationModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.feature.home.addEvent.domain.model.mockGetTaskTypeUIModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
internal fun AddEventScreenContent(
    state: AddEventScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (AddEventScreenContract.Event) -> Unit,
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            BasicTextField(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
                value = state.textEventName.value,
                onValueChange = {
                    setEvent.invoke(AddEventScreenContract.Event.SetEventName(it))
                },
                singleLine = true,
                maxLines = 1,
                minLines = 1,
                textStyle = AppTheme.typography.heading04,
                cursorBrush = SolidColor(AppTheme.colors.generalColors.textPrimary),
                decorationBox = { innerTextField ->
                    Column(
                        modifier = Modifier,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(56.dp)
                                .background(AppTheme.colors.generalColors.transparent),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (state.textEventName.value.isEmpty()) {
                                Text(
                                    text = "Etkinlik Başlığı", // TODO: Localize
                                    style = AppTheme.typography.heading04,
                                    color = AppTheme.colors.generalColors.textDisabled,
                                )
                            }
                            innerTextField()
                        }

                        HorizontalDivider()
                    }
                },
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_time),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.textPrimary),
                )

                Text(
                    text = "Başlangıç Tarihi ve Saati", // TODO: Localize
                    style = AppTheme.typography.subtitleLarge,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmallest),
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Tam gün", // TODO: Localize
                        style = AppTheme.typography.bodyLargeCompact,
                        modifier = Modifier
                            .weight(1f),
                    )
                    Switch(
                        checked = state.switchEventDateAllDay,
                        onCheckedChange = {
                            setEvent.invoke(AddEventScreenContract.Event.SetSwitchEventDateAllDay(it))
                        },
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                DateTimeRow(
                    allDay = state.switchEventDateAllDay,
                    label = "Başlangıç", // TODO: Localize
                    dateText = state.eventStartDate.format(dateFormatter),
                    timeText = state.eventStartTime.format(timeFormatter),
                    onDateClick = {
                        setEvent.invoke(AddEventScreenContract.Event.ShowStartDatePicker)
                    },
                    onTimeClick = {
                        setEvent.invoke(AddEventScreenContract.Event.ShowStartTimePicker)
                    },
                )

                Spacer(modifier = Modifier.height(12.dp))

                DateTimeRow(
                    allDay = state.switchEventDateAllDay,
                    label = "Bitiş", // TODO: Localize
                    dateText = state.eventEndDate.format(dateFormatter),
                    timeText = state.eventEndTime.format(timeFormatter),
                    onDateClick = {
                        setEvent.invoke(AddEventScreenContract.Event.ShowEndDatePicker)
                    },
                    onTimeClick = {
                        setEvent.invoke(AddEventScreenContract.Event.ShowEndTimePicker)
                    },
                )

                Column(
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingMedium)
                        .clip(AppTheme.shapes.radiusMedium)
                        .background(AppTheme.colors.generalColors.backgroundWeak100)
                        .padding(horizontal = AppTheme.spacing.spacingMedium)
                        .padding(vertical = AppTheme.spacing.spacingSmallest),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Etkinliği Tekrarla", // TODO: Localize
                            style = AppTheme.typography.bodyLargeCompact,
                            modifier = Modifier
                                .weight(1f),
                        )
                        Switch(
                            checked = state.switchEventRepeat,
                            onCheckedChange = {
                                setEvent.invoke(AddEventScreenContract.Event.SetSwitchEventRepeat(it))
                            },
                        )
                    }

                    if (state.switchEventRepeat) {
                        RepeatOptionsFlow(
                            modifier = Modifier
                                .padding(bottom = AppTheme.spacing.spacingMedium),
                            state = state,
                            setEvent = setEvent,
                        )
                    }
                }
            }

            HorizontalDivider()
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "Etkinlik Türü", // TODO: Localize
                    style = AppTheme.typography.subtitleLarge,
                    color = AppTheme.colors.generalColors.textPrimary,
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    state.taskTypes.forEach { taskType ->
                        if (taskType == state.selectedTaskType) {
                            Row(
                                modifier = Modifier
                                    .padding(top = AppTheme.spacing.spacingSmallest)
                                    .padding(end = AppTheme.spacing.spacingSmallest)
                                    .clip(AppTheme.shapes.radiusMedium)
                                    .clickable {
                                        setEvent.invoke(AddEventScreenContract.Event.SetSelectedTaskType(taskType))
                                    }
                                    .background(AppTheme.colors.generalColors.textPrimary)
                                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                                    .padding(vertical = AppTheme.spacing.spacingSmallest),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(AppTheme.shapes.radiusCircle)
                                        .background(Color(taskType.color)),
                                )

                                Text(
                                    text = taskType.name ?: "",
                                    modifier = Modifier
                                        .padding(start = 8.dp),
                                    color = AppTheme.colors.generalColors.foregroundWhite,
                                )

                                Image(
                                    painter = painterResource(resourcesR.drawable.img_radio_button_selected_check),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
                                    modifier = Modifier
                                        .padding(start = AppTheme.spacing.spacingSmallest)
                                        .size(16.dp),
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .padding(top = AppTheme.spacing.spacingSmallest)
                                    .padding(end = AppTheme.spacing.spacingSmallest)
                                    .clip(AppTheme.shapes.radiusMedium)
                                    .border(
                                        width = AppTheme.dimens.dp1,
                                        color = AppTheme.colors.generalColors.borderSoft200,
                                        shape = AppTheme.shapes.radiusMedium,
                                    )
                                    .clickable {
                                        setEvent.invoke(AddEventScreenContract.Event.SetSelectedTaskType(taskType))
                                    }
                                    .background(AppTheme.colors.generalColors.backgroundPrimary)
                                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                                    .padding(vertical = AppTheme.spacing.spacingSmallest),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(AppTheme.shapes.radiusCircle)
                                        .background(Color(taskType.color)),
                                )

                                Text(
                                    text = taskType.name ?: "",
                                    modifier = Modifier
                                        .padding(start = 8.dp),
                                )

                                Image(
                                    painter = painterResource(resourcesR.drawable.img_plus),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.textPrimary),
                                    modifier = Modifier
                                        .padding(start = AppTheme.spacing.spacingSmallest)
                                        .size(16.dp),
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Aradığını bulamadın mı? Kendi etkinliğinizi tanımlayın.", // TODO: Localize
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                )

                AppButton.OutlineMedium(
                    text = "Etkinlik Türü Ekle", // TODO: Localize
                    onClick = {
                        setEvent.invoke(AddEventScreenContract.Event.ShowAddTaskTypeDialog)
                    },
                    leftIcon = resourcesR.drawable.img_plus,
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular),
                )
            }

            HorizontalDivider()
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        setEvent.invoke(AddEventScreenContract.Event.ShowAddUserBottomSheet)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_user_plus),
                    contentDescription = null,
                )

                Text(
                    text = "Kişi/Topluluk Ekle", // TODO: Localize
                    style = AppTheme.typography.subtitleLarge,
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = 8.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

                state.selectedGetTrainingGroupUserUIModelTeam?.let { item ->
                    AppAsyncImageLoader.Load(
                        data = item.detail,
                        modifier = Modifier
                            .size(AppTheme.dimens.dp18)
                            .background(
                                color = AppTheme.colors.generalColors.backgroundSoft200,
                                shape = AppTheme.shapes.radiusCircle,
                            )
                            .clip(AppTheme.shapes.radiusCircle),
                        contentScale = ContentScale.Crop,
                    )
                }

                state.selectedGetTrainingGroupUserList.let { users ->
                    Box(
                        modifier = Modifier,
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
                                        data = item.imageUrl ?: item.name.getUserNameFirstChar(),
                                        modifier = Modifier
                                            .size(AppTheme.dimens.dp18)
                                            .background(
                                                color = AppTheme.colors.generalColors.backgroundSoft200,
                                                shape = AppTheme.shapes.radiusCircle,
                                            )
                                            .clip(AppTheme.shapes.radiusCircle),
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            }
                        }
                    }
                }

                Image(
                    painter = painterResource(resourcesR.drawable.img_arrow_right),
                    contentDescription = null,
                )
            }

            HorizontalDivider()
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        setEvent.invoke(AddEventScreenContract.Event.NavigateToSelectAddress)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Image(
                    painter = painterResource(resourcesR.drawable.img_location_pin),
                    contentDescription = null,
                )

                Column {
                    Text(
                        text = "Lokasyon", // TODO: Localize
                        style = AppTheme.typography.subtitleLarge,
                        color = AppTheme.colors.generalColors.textPrimary,
                        modifier = Modifier
                            .padding(start = 8.dp),
                    )

                    state.addressTitle?.let {
                        Text(
                            text = it, // TODO: Localize
                            style = AppTheme.typography.subtitleLarge,
                            color = AppTheme.colors.generalColors.textPrimary,
                            modifier = Modifier
                                .padding(start = 8.dp),
                        )

                        Text(
                            text = "${state.addressDetail}, ${state.city}, ${state.country}", // TODO: Localize
                            modifier = Modifier
                                .padding(start = 8.dp),
                        )
                    }
                }
            }

            HorizontalDivider()
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        setEvent.invoke(AddEventScreenContract.Event.ShowAddDescriptionDialog)
                    }
                    .padding(16.dp),
            ) {
                Row {
                    Text(
                        text = "Açıklama", // TODO: Localize
                        style = AppTheme.typography.subtitleLarge,
                        color = AppTheme.colors.generalColors.textPrimary,
                        modifier = Modifier
                            .clickable {
                                setEvent.invoke(AddEventScreenContract.Event.ShowAddDescriptionDialog)
                            },
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (state.textDescription.isNotEmpty) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    setEvent.invoke(AddEventScreenContract.Event.ShowAddDescriptionDialog)
                                },
                        ) {
                            Image(
                                painter = painterResource(resourcesR.drawable.img_edit_pen),
                                contentDescription = null,
                            )

                            Text(
                                text = "Düzenle", // TODO: Localize
                                style = AppTheme.typography.subtitleLarge,
                                color = AppTheme.colors.generalColors.textPrimary,
                                modifier = Modifier
                                    .clickable {
                                        setEvent.invoke(AddEventScreenContract.Event.ShowAddDescriptionDialog)
                                    },
                            )
                        }   
                    }
                }

                if (state.textDescription.isNotEmpty) {
                    Text(
                        text = state.textDescription.value, // TODO: Localize
                        style = AppTheme.typography.subtitleLarge.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                        color = AppTheme.colors.generalColors.textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                setEvent.invoke(AddEventScreenContract.Event.ShowAddDescriptionDialog)
                            },
                    )
                }
            }

            HorizontalDivider()
        }

        if (state.route.model.task == null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            setEvent.invoke(AddEventScreenContract.Event.ChangeCheckBoxDraftState)
                        }
                        .padding(16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = state.isDraft,
                            onCheckedChange = null,
                            modifier = Modifier
                                .padding(end = AppTheme.spacing.spacingSmallest),
                        )

                        Text(
                            text = "Bu etkinliği daha sonra kullanmak için şablonlara kaydet!", // TODO: Localize
                            style = AppTheme.typography.subtitleLarge.copy(
                                fontWeight = FontWeight.Medium,
                            ),
                            color = AppTheme.colors.generalColors.textPrimary,
                        )
                    }
                }
            }   
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RepeatOptionsFlow(
    state: AddEventScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (AddEventScreenContract.Event) -> Unit,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        state.repeatOptions.forEach { option ->
            if (state.selectedRepeatType == option.key) {
                AppButton.SecondarySmall(
                    text = option.value,
                    onClick = {
                        setEvent.invoke(AddEventScreenContract.Event.SetSelectedRepeatType(option.key))
                    },
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingSmallest)
                        .padding(end = AppTheme.spacing.spacingSmallest),
                )
            } else {
                AppButton.SecondaryWhiteSmall(
                    text = option.value,
                    onClick = {
                        setEvent.invoke(AddEventScreenContract.Event.SetSelectedRepeatType(option.key))
                    },
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingSmallest)
                        .padding(end = AppTheme.spacing.spacingSmallest),
                )
            }
        }
    }
}

@Composable
private fun DateTimeRow(
    allDay: Boolean,
    label: String,
    dateText: String,
    timeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.bodyLargeCompact,
        )
        Box(
            modifier = Modifier
                .clip(AppTheme.shapes.radiusMedium)
                .background(AppTheme.colors.generalColors.backgroundWeak100)
                .clickable(onClick = onDateClick)
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(vertical = AppTheme.spacing.spacingSmallest),
        ) {
            Text(dateText)
        }
        if (!allDay) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(AppTheme.shapes.radiusMedium)
                    .background(AppTheme.colors.generalColors.backgroundWeak100)
                    .clickable(onClick = onTimeClick)
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(vertical = AppTheme.spacing.spacingSmallest),
            ) {
                Text(timeText)
            }
        }
    }
}

@Preview(
    name = "Phone",
    device = "spec:width=411dp,height=1891dp,dpi=420",
)
@PreviewAppWithNightMode()
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            AddEventScreenContent(
                state = AddEventScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenAddEventRoute(
                        model = HomeScreenAddEventScreenNavigationModel(
                            selectedDate = null,
                        ),
                    ),
                    taskTypes = mockGetTaskTypeUIModel,
                    eventStartDate = LocalDate.now(),
                    eventStartTime = LocalTime.now(),
                    eventEndDate = LocalDate.now(),
                    eventEndTime = LocalTime.now().plusHours(2),
                    textEventName = AppTextFieldValue(
                        value = "",
                    ),
                    switchEventRepeat = true,
                    selectedTaskType = mockGetTaskTypeUIModel.firstOrNull(),
                    textDescription = AppTextFieldValue(value = "asdasdasd"),
                ),
                setEvent = { },
            )
        }
    }
}
