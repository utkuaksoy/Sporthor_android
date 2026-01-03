package com.iamkurtgoz.feature.home.inviteGroupMembers.component

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.feature.home.inviteGroupMembers.InviteGroupMembersScreenContract
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.CoachRelationUIItemModel
import com.iamkurtgoz.feature.home.inviteGroupMembers.domain.model.SocialSearchUIItemModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CoachRoleSelectionBottomSheet(
    state: InviteGroupMembersScreenContract.State,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.showCoachRoleSelectionBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = {
                setEvent(InviteGroupMembersScreenContract.Event.DismissCoachRoleSelection)
            },
            sheetState = sheetState,
            containerColor = Color.White,
            scrimColor = Color.Black.copy(alpha = 0.5f),
            modifier = modifier,
        ) {
            CoachRoleSelectionContent(
                state = state,
                setEvent = setEvent,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoachRoleSelectionContent(
    state: InviteGroupMembersScreenContract.State,
    setEvent: (InviteGroupMembersScreenContract.Event) -> Unit,
) {
    val coach = state.selectedCoachForRoleSelection
    val name = when (coach) {
        is CoachRelationUIItemModel -> coach.name
        is SocialSearchUIItemModel -> coach.name
        else -> ""
    }
    val image = when (coach) {
        is CoachRelationUIItemModel -> coach.imageUrl
        is SocialSearchUIItemModel -> coach.image
        else -> null
    }

    val roles = listOf(
        "Baş Antrenör",
        "Yardımcı Antrenör",
        "Fizyoterapist",
        "Masör",
        "Kondisyoner",
        "İstatistik Antrenörü",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.spacingLarge)
            .padding(bottom = AppTheme.spacing.spacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Handle bar styling if needed, but ModalBottomSheet has default

        Text(
            text = "Antrenör Rolünü Belirle",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingLarge))

        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center,
        ) {
            if (!image.isNullOrBlank()) {
                AppAsyncImageLoader.Load(
                    data = image,
                    modifier = Modifier.fillMaxWidth().clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    text = name.getUserNameFirstChar(),
                    style = AppTheme.typography.heading03,
                    color = Color.White,
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium))

        Text(
            text = name ?: "",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingLarge))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Yardımcı Antrenör", // Title for the section, mimicking design
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = AppTheme.spacing.spacingMedium),
            )

            // Thin divider
            Box(
                modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE5E5E5)),
            )
            Spacer(modifier = Modifier.height(AppTheme.spacing.spacingMedium))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                roles.forEach { role ->
                    val isSelected = state.selectedCoachRole == role
                    RoleChip(
                        text = role,
                        isSelected = isSelected,
                        onClick = {
                            setEvent(InviteGroupMembersScreenContract.Event.SetCoachRole(role))
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AppButton.PrimaryLarge(
            text = "Devam Et",
            onClick = {
                setEvent(InviteGroupMembersScreenContract.Event.ConfirmCoachRoleSelection)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.spacingLarge))
    }
}

@Composable
private fun RoleChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) Color(0xFFDAF683) else Color.White
    val borderColor = if (isSelected) Color(0xFFDAF683) else Color(0xFFE5E5E5)

    Box(
        modifier = Modifier
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = AppTheme.typography.bodyMedium,
                color = Color.Black,
            )
            // If we wanted the checkmark icon, we could add it here for selected state
        }
    }
}
