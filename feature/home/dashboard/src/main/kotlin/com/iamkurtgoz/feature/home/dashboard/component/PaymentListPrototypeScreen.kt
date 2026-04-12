package com.iamkurtgoz.feature.home.dashboard.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface

@Composable
internal fun PaymentListPrototypeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    val filterItems = remember {
        listOf("Tümü", "Ödenenler", "Ödenmemiş", "İptal Edilen")
    }
    var selectedFilter by remember { mutableStateOf(filterItems.first()) }

    var expandedGroupIds by remember { mutableStateOf(setOf("eczacibasi-midi")) }

    val paymentGroups = remember { samplePaymentGroups() }

    Column(
        modifier = modifier,
    ) {
        Header(
            title = "Ödeme Listesi",
            onBackClick = onBackClick,
        )

        HorizontalDivider(
            color = AppTheme.colors.generalColors.foregroundDisabled,
            thickness = 1.dp,
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            filterItems.forEach { item ->
                FilterChip(
                    text = item,
                    isSelected = selectedFilter == item,
                    onClick = { selectedFilter = item },
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(paymentGroups, key = { it.id }) { group ->
                val isExpanded = expandedGroupIds.contains(group.id)
                PaymentGroupCard(
                    group = group,
                    isExpanded = isExpanded,
                    onHeaderClick = {
                        expandedGroupIds = if (isExpanded) {
                            expandedGroupIds - group.id
                        } else {
                            expandedGroupIds + group.id
                        }
                    },
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun Header(
    title: String,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Geri",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onBackClick() }
                .padding(8.dp),
            tint = AppTheme.colors.generalColors.foregroundPrimary,
        )

        Text(
            text = title,
            style = AppTheme.typography.subtitleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 10.dp),
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .clickable { onClick() },
        color = if (isSelected) Color(0xFFA5F55D) else Color(0xFFF2F2F2),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color.Transparent else Color(0xFFE6E6E6),
        ),
    ) {
        Text(
            text = text,
            style = AppTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            color = Color(0xFF1B1B1B),
        )
    }
}

@Composable
private fun PaymentGroupCard(
    group: PaymentGroup,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE7E7E7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHeaderClick() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LogoCircle(initial = group.teamName.firstOrNull()?.toString().orEmpty())
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.teamName,
                        style = AppTheme.typography.subtitleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        StatusWithIcon(
                            icon = Icons.Filled.CheckCircle,
                            iconTint = Color(0xFF3D7E30),
                            text = "${group.completedCount} Tamamlanan",
                        )
                        StatusWithIcon(
                            icon = Icons.Filled.Warning,
                            iconTint = Color(0xFFF35B54),
                            text = "${group.overdueCount} Gecikmiş Ödeme",
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Aç/Kapat",
                    tint = Color(0xFF282828),
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFECECEC))
                Spacer(modifier = Modifier.height(4.dp))

                group.players.forEachIndexed { index, player ->
                    PlayerRow(player = player)
                    if (index != group.players.lastIndex) {
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerRow(player: PlayerPayment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarCircle(name = player.name)
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = player.name,
                style = AppTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            if (player.isOverdue) {
                Spacer(modifier = Modifier.height(2.dp))
                StatusWithIcon(
                    icon = Icons.Filled.Warning,
                    iconTint = Color(0xFFF35B54),
                    text = "Gecikmiş Ödeme",
                )
            }
        }

        if (player.balance != null) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.wrapContentWidth(),
            ) {
                Text(
                    text = "Bakiye",
                    style = AppTheme.typography.labelMedium,
                    color = Color(0xFF666666),
                )
                Text(
                    text = player.balance,
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Detay",
            tint = Color(0xFF353535),
        )
    }
}

@Composable
private fun StatusWithIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = AppTheme.typography.labelMedium,
            color = Color(0xFF4D4D4D),
        )
    }
}

@Composable
private fun LogoCircle(initial: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xFFF08E20)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial,
            style = AppTheme.typography.subtitleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}

@Composable
private fun AvatarCircle(name: String) {
    val initials = remember(name) {
        name.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString(separator = "")
    }

    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = AppTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5A5A5A),
        )
    }
}

private data class PaymentGroup(
    val id: String,
    val teamName: String,
    val completedCount: Int,
    val overdueCount: Int,
    val players: List<PlayerPayment>,
)

private data class PlayerPayment(
    val name: String,
    val isOverdue: Boolean,
    val balance: String? = null,
)

private fun samplePaymentGroups(): List<PaymentGroup> {
    return listOf(
        PaymentGroup(
            id = "eczacibasi-midi",
            teamName = "Eczacıbaşı Midi Kız",
            completedCount = 4,
            overdueCount = 1,
            players = listOf(
                PlayerPayment(name = "Emine Türk", isOverdue = true, balance = "2800 TL"),
                PlayerPayment(name = "Cansu Bilgi", isOverdue = false),
                PlayerPayment(name = "Ayça Türkyılmaz", isOverdue = false),
                PlayerPayment(name = "Gizem Leyla Tuna", isOverdue = false),
                PlayerPayment(name = "Yeşim Karalı", isOverdue = false),
            ),
        ),
        PaymentGroup(
            id = "eczacibasi-yildiz",
            teamName = "Eczacıbaşı Yıldız Kız",
            completedCount = 8,
            overdueCount = 2,
            players = emptyList(),
        ),
    )
}

@PreviewAppWithNightMode
@Composable
private fun PaymentListPrototypeScreenPreview() {
    AppTheme {
        AppThemeSurface {
            PaymentListPrototypeScreen()
        }
    }
}
