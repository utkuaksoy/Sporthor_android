package com.iamkurtgoz.feature.home.profile.settings.accountSettings.blockedUsers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface

@Composable
internal fun BlockedUsersScreenContent(
    state: BlockedUsersScreenContract.State,
    onRemoveBlockedUser: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.blockedUsers.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.spacing.spacingMedium),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Engellenen kullanıcı bulunmamaktadır.",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.generalColors.textSecondary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = AppTheme.spacing.spacingMedium),
    ) {
        items(
            items = state.blockedUsers,
            key = { it.id },
        ) { user ->
            val isRemoving = state.removingUserIds.contains(user.id)
            val iconBackground = if (isRemoving) {
                AppTheme.colors.generalColors.backgroundDisabled
            } else {
                Color(0xFF9BEA57)
            }
            val iconTint = if (isRemoving) {
                AppTheme.colors.generalColors.foregroundDisabled
            } else {
                Color.Black
            }
            UserRow(
                userHeaderData = user.imageUrl ?: user.name.firstOrNull()?.toString(),
                isHeaderUser = true,
                title = user.name,
                subTitle = arrayOf(user.username.orEmpty()),
                contentPadding = PaddingValues(vertical = AppTheme.spacing.spacingSmall),
                trailingContent = {
                    Row(
                        modifier = Modifier
                            .size(28.dp)
                            .background(iconBackground, CircleShape)
                            .clickable(enabled = !isRemoving) {
                                onRemoveBlockedUser(user.id)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Blocked",
                            tint = iconTint,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                },
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            BlockedUsersScreenContent(
                state = BlockedUsersScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppTheme.appBuildConfigStatePack,
                    appRemoteConfigStatePack = AppTheme.appRemoteConfigStatePack,
                    blockedUsers = listOf(
                        BlockedUserUiModel(
                            id = "1",
                            name = "Spr1 Spr1",
                            username = "spr1",
                        ),
                    ),
                ),
                onRemoveBlockedUser = {},
            )
        }
    }
}
