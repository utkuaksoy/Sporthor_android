package com.iamkurtgoz.feature.home.paymentList

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

internal data class PaymentGroupUi(
    val id: String,
    val teamName: String,
    val teamSubtitle: String,
    val teamPhotoUrl: String? = null,
    val completedCount: Int,
    val overdueCount: Int,
    val players: List<PlayerPaymentUi>,
)

internal data class PlayerPaymentUi(
    val userId: String,
    val trainingGroupId: String,
    val name: String,
    val profilePhotoUrl: String? = null,
    val isOverdue: Boolean,
    val balance: String? = null,
)

@Composable
internal fun PaymentGroupCard(
    group: PaymentGroupUi,
    isExpanded: Boolean,
    showAddAction: Boolean,
    onHeaderClick: () -> Unit,
    onAddClick: () -> Unit,
    onPlayerClick: (PlayerPaymentUi) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE7E7E7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHeaderClick() },
                verticalAlignment = Alignment.Top,
            ) {
                LogoCircle(
                    imageUrl = group.teamPhotoUrl,
                    fallbackText = group.teamName.firstOrNull()?.toString().orEmpty(),
                )
                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = group.teamName,
                        style = AppTheme.typography.heading06,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = group.teamSubtitle,
                        style = AppTheme.typography.bodyMedium,
                        color = Color(0xFF5D5D5D),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        StatusBadge(
                            text = stringResource(resourcesR.string.paymentlist_badge_overdue, group.overdueCount),
                            containerColor = Color(0xFFF8DEDE),
                            contentColor = Color(0xFF8C3939),
                        )
                        StatusBadge(
                            text = stringResource(resourcesR.string.paymentlist_badge_paid, group.completedCount),
                            containerColor = Color(0xFFE6F2DA),
                            contentColor = Color(0xFF4C7632),
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    if (showAddAction) {
                        ActionIcon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(resourcesR.string.paymentlist_action_add_payment),
                            onClick = onAddClick,
                        )
                    }
                    ActionIcon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(resourcesR.string.paymentlist_action_expand_collapse),
                        onClick = onHeaderClick,
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFEDEDED))
                Spacer(modifier = Modifier.height(8.dp))

                group.players.forEachIndexed { index, player ->
                    PlayerRow(
                        player = player,
                        onClick = { onPlayerClick(player) },
                    )
                    if (index != group.players.lastIndex) {
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEFF2F4))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = Color(0xFF1D1D1D),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            style = AppTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PlayerRow(
    player: PlayerPaymentUi,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarCircle(
            name = player.name,
            imageUrl = player.profilePhotoUrl,
        )
        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = player.name,
            style = AppTheme.typography.subtitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (player.isOverdue) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = Color(0xFFF35B54),
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = player.balance ?: "-",
            style = AppTheme.typography.subtitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF181818),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun LogoCircle(
    imageUrl: String?,
    fallbackText: String,
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(Color(0xFFD28736)),
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
                text = fallbackText,
                style = AppTheme.typography.subtitleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun AvatarCircle(
    name: String,
    imageUrl: String?,
) {
    val initials = remember(name) {
        name.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString(separator = "")
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color(0xFFEFEFEF)),
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
                text = initials,
                style = AppTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A5A5A),
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun PaymentGroupCardPreview() {
    AppTheme {
        AppThemeSurface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PaymentGroupCard(
                    group = PaymentGroupUi(
                        id = "grp_101",
                        teamName = "Eczacıbaşı Spor Kulübü",
                        teamSubtitle = "Yıldız Kız - Eczacıbaşı C",
                        teamPhotoUrl = null,
                        completedCount = 4,
                        overdueCount = 1,
                        players = listOf(
                            PlayerPaymentUi(
                                userId = "usr_1",
                                trainingGroupId = "grp_101",
                                name = "Emine Türk",
                                profilePhotoUrl = null,
                                isOverdue = true,
                                balance = "40.000 TL",
                            ),
                            PlayerPaymentUi(
                                userId = "usr_2",
                                trainingGroupId = "grp_101",
                                name = "Cansu Bilgi",
                                profilePhotoUrl = null,
                                isOverdue = false,
                                balance = "20.000 TL",
                            ),
                        ),
                    ),
                    isExpanded = true,
                    showAddAction = true,
                    onHeaderClick = {},
                    onAddClick = {},
                    onPlayerClick = {},
                )

                PaymentGroupCard(
                    group = PaymentGroupUi(
                        id = "grp_102",
                        teamName = "Eczacıbaşı Spor Kulübü",
                        teamSubtitle = "Yıldız Kız - Eczacıbaşı B",
                        teamPhotoUrl = null,
                        completedCount = 8,
                        overdueCount = 1,
                        players = emptyList(),
                    ),
                    isExpanded = false,
                    showAddAction = true,
                    onHeaderClick = {},
                    onAddClick = {},
                    onPlayerClick = {},
                )
            }
        }
    }
}
