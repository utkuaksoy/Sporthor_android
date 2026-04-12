package com.iamkurtgoz.feature.home.paymentList.addfee

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.resources.R as resourcesR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddFeeUserOrGroupPickerSheet(
    state: AddFeeViewModel.State,
    setEvent: (AddFeeViewModel.Event) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },
    )

    val filteredGroups = state.groups.filter { group ->
        state.searchText.isBlank() || group.name.contains(state.searchText, ignoreCase = true)
    }
    val filteredUsers = state.users.filter { user ->
        state.searchText.isBlank() || user.isMatch(state.searchText)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
        ) {
            Text(
                text = stringResource(resourcesR.string.addfeescreen_picker_title),
                style = AppTheme.typography.heading05,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.searchText,
                onValueChange = {
                    setEvent(AddFeeViewModel.Event.SetSearchText(it))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                    )
                },
                placeholder = {
                    Text(text = stringResource(resourcesR.string.addfeescreen_picker_search_placeholder))
                },
                singleLine = true,
            )

            if (filteredGroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(resourcesR.string.addfeescreen_picker_groups),
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))

                filteredGroups.forEach { group ->
                    PickerGroupRow(
                        item = group,
                        selected = state.selectedTrainingGroupIds.contains(group.id),
                        onClick = {
                            setEvent(AddFeeViewModel.Event.ToggleTrainingGroupSelection(group.id))
                        },
                    )
                }
            }

            if (filteredUsers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(resourcesR.string.addfeescreen_picker_people),
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))

                filteredUsers.forEachIndexed { index, user ->
                    PickerUserRow(
                        item = user,
                        selected = state.selectedUserSelectionKeys.contains(user.selectionKey),
                        onClick = {
                            setEvent(AddFeeViewModel.Event.ToggleUserSelection(user))
                        },
                    )

                    if (index != filteredUsers.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 68.dp),
                            color = Color(0xFFF0F0F0),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    setEvent(AddFeeViewModel.Event.SetPersonOrGroupPickerVisible(false))
                },
                enabled = state.selectedGroups.isNotEmpty() || state.selectedUsers.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA7F45C),
                    contentColor = Color(0xFF101010),
                ),
            ) {
                Text(
                    text = stringResource(resourcesR.string.addfeescreen_picker_save),
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun PickerGroupRow(
    item: AddFeeSelectableGroupUi,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PickerAvatar(
            imageUrl = item.imageUrl,
            fallbackText = item.name.take(1),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = AppTheme.typography.subtitleLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (item.subtitle.isNotBlank()) {
                Text(
                    text = item.subtitle,
                    style = AppTheme.typography.bodyMedium,
                    color = Color(0xFF6D6D6D),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
        )
    }
}

@Composable
private fun PickerUserRow(
    item: AddFeeSelectableUserUi,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PickerAvatar(
            imageUrl = item.imageUrl,
            fallbackText = item.name.take(2),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = AppTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )

            if (!item.username.isNullOrBlank()) {
                Text(
                    text = "@${item.username}",
                    style = AppTheme.typography.bodyMedium,
                    color = Color(0xFF6D6D6D),
                )
            }
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
        )
    }
}

@Composable
private fun PickerAvatar(
    imageUrl: String?,
    fallbackText: String,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AppAsyncImageLoader.Load(
                data = imageUrl,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = fallbackText.ifBlank { "?" },
                style = AppTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A5A5A),
            )
        }
    }
}
