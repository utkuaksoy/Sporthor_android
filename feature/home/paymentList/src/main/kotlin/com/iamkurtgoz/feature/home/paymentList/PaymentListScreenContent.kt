package com.iamkurtgoz.feature.home.paymentList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.iamkurtgoz.domain.model.response.FeeGroupDomainModel
import com.iamkurtgoz.domain.model.response.FeeUserDomainModel
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun PaymentListScreenContent(
    modifier: Modifier = Modifier,
    state: PaymentListScreenContract.State,
    setEvent: (PaymentListScreenContract.Event) -> Unit,
) {
    val filters = listOf(
        FilterUiItem(stringResource(resourcesR.string.paymentlist_filter_all), 0),
        FilterUiItem(stringResource(resourcesR.string.paymentlist_filter_paid), 1),
        FilterUiItem(stringResource(resourcesR.string.paymentlist_filter_unpaid), 2),
        FilterUiItem(stringResource(resourcesR.string.paymentlist_filter_cancelled), 3),
    )
    var expandedGroupIds by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {



        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            filters.forEach { item ->
                val isSelected = state.selectedFilterType == item.filterType
                FilterChip(
                    text = item.text,
                    isSelected = isSelected,
                    onClick = {
                        setEvent(
                            PaymentListScreenContract.Event.ChangeFilter(
                                filterType = item.filterType,
                            ),
                        )
                    },
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(state.feeResponse?.fees.orEmpty(), key = { it.trainingGroupId ?: it.trainingGroupName.orEmpty() }) { group ->
                val groupId = group.trainingGroupId ?: group.trainingGroupName.orEmpty()
                val isExpanded = expandedGroupIds.contains(groupId)

                PaymentGroupCard(
                    group = group.toUiGroup(),
                    isExpanded = isExpanded,
                    showAddAction = state.feeResponse?.isShowPlus == true,
                    onHeaderClick = {
                        expandedGroupIds = if (isExpanded) {
                            expandedGroupIds - groupId
                        } else {
                            expandedGroupIds + groupId
                        }
                    },
                    onAddClick = {
                        setEvent(
                            PaymentListScreenContract.Event.NavigateToAddFee(
                                trainingGroupId = groupId,
                            ),
                        )
                    },
                    onPlayerClick = { player ->
                        if (player.userId.isNotBlank() && player.trainingGroupId.isNotBlank()) {
                            setEvent(
                                PaymentListScreenContract.Event.NavigateToFeeDetail(
                                    userId = player.userId,
                                    trainingGroupId = player.trainingGroupId,
                                ),
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (isSelected) Color(0xFFA5F55D) else Color(0xFFF2F2F2))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 11.dp),
        style = AppTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1B1B1B),
    )
}

private data class FilterUiItem(
    val text: String,
    val filterType: Int,
)

@Composable
private fun FeeGroupDomainModel.toUiGroup(): PaymentGroupUi {
    val rawGroupName = trainingGroupName.orEmpty().ifBlank { stringResource(resourcesR.string.paymentlist_unknown_club) }
    val parsedTeamName = rawGroupName.substringBefore(" - ", missingDelimiterValue = rawGroupName)
    val parsedSubtitle = rawGroupName.substringAfter(" - ", missingDelimiterValue = "")

    return PaymentGroupUi(
        id = trainingGroupId ?: trainingGroupName.orEmpty(),
        teamName = parsedTeamName,
        teamSubtitle = parsedSubtitle.ifBlank {
            stringResource(resourcesR.string.paymentlist_group_prefix, trainingGroupId ?: "-")
        },
        teamPhotoUrl = trainingGroupPhotoUrl,
        completedCount = summary?.completedCount ?: 0,
        overdueCount = summary?.overdueCount ?: 0,
        players = users.orEmpty().map { user ->
            user.toUiPlayer(trainingGroupId = trainingGroupId.orEmpty())
        },
    )
}

@Composable
private fun FeeUserDomainModel.toUiPlayer(
    trainingGroupId: String,
): PlayerPaymentUi {
    val resolvedName = fullName.orEmpty().ifBlank {
        stringResource(resourcesR.string.paymentlist_unnamed_athlete)
    }
    val resolvedBalance = balanceText
        ?: balance?.let { amount ->
            val formattedAmount = if (amount % 1.0 == 0.0) {
                amount.toInt().toString()
            } else {
                amount.toString()
            }
            "$formattedAmount ${currency ?: "TRY"}"
        }
        ?: "-"

    return PlayerPaymentUi(
        userId = userId.orEmpty(),
        trainingGroupId = trainingGroupId,
        name = resolvedName,
        profilePhotoUrl = profilePhoto,
        isOverdue = (paymentStatus ?: 0) == 2,
        balance = resolvedBalance,
    )
}

@PreviewAppWithNightMode
@Composable
private fun PaymentListScreenPreview() {
    AppTheme {
        AppThemeSurface {
            PaymentListScreenContent(
                state = PaymentListScreenContract.State(),
                setEvent = {},
            )
        }
    }
}
