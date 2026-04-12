package com.iamkurtgoz.feature.home.paymentList.detail

import android.R as androidR
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.response.FeeDetailItemDomainModel
import com.iamkurtgoz.domain.model.response.FeeDetailSeasonDomainModel
import com.iamkurtgoz.domain.model.response.FeeDetailUserDomainModel
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun FeeDetailScreen(
    userId: String,
    trainingGroupId: String,
    navigateUp: () -> Unit,
    navigateToAddFee: (String) -> Unit,
    viewModel: FeeDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(userId, trainingGroupId) {
        viewModel.setEvent(
            FeeDetailViewModel.Event.Initialize(
                userId = userId,
                trainingGroupId = trainingGroupId,
            ),
        )
    }

    viewModel.sideEffect.observeSideEffect { effect ->
        when (effect) {
            FeeDetailViewModel.SideEffect.NavigateUp -> navigateUp()
            is FeeDetailViewModel.SideEffect.NavigateToAddFee -> navigateToAddFee(effect.trainingGroupId)
            is FeeDetailViewModel.SideEffect.ShowNotificationToast -> {
                Toast.makeText(
                    context,
                    context.getString(
                        if (effect.isOverdue) {
                            resourcesR.string.paymentdetail_overdue_notification_sent_toast
                        } else {
                            resourcesR.string.paymentdetail_pending_notification_sent_toast
                        },
                    ),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    FeeDetailScreenContent(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeeDetailScreenContent(
    state: FeeDetailViewModel.State,
    setEvent: (FeeDetailViewModel.Event) -> Unit,
) {
    val user = state.detail?.user
    val seasons = state.detail?.seasons.orEmpty().filter { !it.seasonName.isNullOrBlank() || !it.fees.isNullOrEmpty() }
    var selectedSeasonId by rememberSaveable(seasons) {
        mutableStateOf(seasons.firstOrNull()?.seasonId.orEmpty())
    }
    var showTotalSheet by rememberSaveable { mutableStateOf(false) }
    var selectedFee by remember { mutableStateOf<FeeDetailItemDomainModel?>(null) }
    val selectedSeason = seasons.firstOrNull { it.seasonId == selectedSeasonId } ?: seasons.firstOrNull()
    val selectedFees = selectedSeason?.fees.orEmpty()
    val hasFees = selectedFees.isNotEmpty()
    val totalBalanceText = selectedFees.sumOf { it.balance ?: 0.0 }.toTryAmountText()
    val completedTotalText = selectedFees.filter { it.paymentStatus == 1 }.sumOf { it.balance ?: 0.0 }.toTryAmountText()
    val overdueTotalText = selectedFees.filter { it.paymentStatus == 2 }.sumOf { it.balance ?: 0.0 }.toTryAmountText()
    val pendingTotalText = selectedFees.filter { it.paymentStatus != 1 && it.paymentStatus != 2 }.sumOf { it.balance ?: 0.0 }.toTryAmountText()

    LaunchedEffect(state.markAsPaidSuccessToken) {
        if (state.markAsPaidSuccessToken > 0) {
            selectedFee = null
        }
    }

    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = { setEvent(FeeDetailViewModel.Event.NavigateUp) },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = stringResource(resourcesR.string.paymentdetail_title),
                    )
                },
                rightContent = {
                    AppToolbarFields.ImageIcon(
                        resId = androidR.drawable.ic_input_add,
                        onClick = { setEvent(FeeDetailViewModel.Event.NavigateToAddFee) },
                    )
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = if (hasFees) 96.dp else 24.dp),
            ) {
                FeeDetailHeader(user = user)

                Spacer(modifier = Modifier.height(18.dp))

                if (seasons.isNotEmpty()) {
                    SeasonTabs(
                        seasons = seasons,
                        selectedSeasonId = selectedSeason?.seasonId.orEmpty(),
                        onSeasonSelected = { selectedSeasonId = it },
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    FeeTableHeader()
                    HorizontalDivider(color = Color(0xFFE8E8E8))
                }

                if (hasFees) {
                    selectedFees.forEach { fee ->
                        SwipeRevealFeeRow {
                            FeeTableRow(
                                fee = fee,
                                onClick = { selectedFee = fee },
                            )
                        }
                    }
                } else {
                    FeeEmptyState()
                }
            }

            if (hasFees) {
                FeeTotalBar(
                    totalText = totalBalanceText,
                    onClick = { showTotalSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                )
            }

            if (state.isLoading) {
                AppLoadingDialog()
            }
        }

        if (showTotalSheet && hasFees) {
            FeeTotalBottomSheet(
                completedTotalText = completedTotalText,
                pendingTotalText = pendingTotalText,
                overdueTotalText = overdueTotalText,
                totalText = totalBalanceText,
                onDismissRequest = { showTotalSheet = false },
            )
        }

        selectedFee?.let { fee ->
            FeeStatusBottomSheet(
                fee = fee,
                onReminderClick = {
                    setEvent(
                        FeeDetailViewModel.Event.SendFeePaymentNotification(
                            feeId = fee.id.orEmpty(),
                            isOverdue = fee.paymentStatus == 2,
                        ),
                    )
                },
                onMarkAsPaidClick = {
                    setEvent(
                        FeeDetailViewModel.Event.MarkFeeAsPaid(
                            feeId = fee.id.orEmpty(),
                        ),
                    )
                },
                onDismissRequest = { selectedFee = null },
            )
        }
    }
}

@Composable
private fun FeeDetailHeader(
    user: FeeDetailUserDomainModel?,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        FeeDetailAvatar(
            imageUrl = user?.profilePhoto,
            fallbackText = user?.fullName.orEmpty(),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = user?.fullName.orEmpty(),
                style = AppTheme.typography.heading05,
                fontWeight = FontWeight.Bold,
            )

            HeaderInfoRow(
                label = stringResource(resourcesR.string.paymentdetail_training_group),
                value = user?.trainingGroupName.orEmpty(),
            )

            HeaderInfoRow(
                label = stringResource(resourcesR.string.paymentdetail_registration_date),
                value = user?.registrationDate.formatDisplayDate(),
            )
        }
    }
}

@Composable
private fun HeaderInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyLarge,
            color = Color(0xFF4C4C4C),
            maxLines = 1,
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1D1D),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SeasonTabs(
    seasons: List<FeeDetailSeasonDomainModel>,
    selectedSeasonId: String,
    onSeasonSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        seasons.forEach { season ->
            val seasonId = season.seasonId.orEmpty()
            val isSelected = seasonId == selectedSeasonId

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .clickable { onSeasonSelected(seasonId) },
                color = if (isSelected) Color(0xFFA7F45C) else Color(0xFFF0F0F0),
            ) {
                Text(
                    text = season.seasonName.orEmpty(),
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF101010) else Color(0xFF6A6A6A),
                )
            }
        }
    }
}

@Composable
private fun FeeTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(resourcesR.string.paymentdetail_column_category),
            modifier = Modifier.weight(1.15f),
            style = AppTheme.typography.bodyLarge,
            color = Color(0xFF666666),
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(resourcesR.string.paymentdetail_column_period),
            modifier = Modifier.weight(1.05f),
            style = AppTheme.typography.bodyLarge,
            color = Color(0xFF666666),
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(resourcesR.string.paymentdetail_column_amount),
            modifier = Modifier.weight(1.45f),
            style = AppTheme.typography.bodyLarge,
            color = Color(0xFF666666),
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.width(28.dp))
    }
}

@Composable
private fun FeeTableRow(
    fee: FeeDetailItemDomainModel,
    onClick: () -> Unit,
) {
    val feeDescription = fee.description

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = fee.categoryName.orEmpty(),
                modifier = Modifier.weight(1.15f),
                style = AppTheme.typography.subtitleLarge,
                color = Color(0xFF191919),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = fee.periodName.orEmpty(),
                modifier = Modifier.weight(1.05f),
                style = AppTheme.typography.subtitleLarge,
                color = Color(0xFF191919),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier.weight(1.45f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PaymentStatusIcon(paymentStatus = fee.paymentStatus)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = fee.balanceText ?: fee.balance.toTryAmountText(),
                    style = AppTheme.typography.subtitleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191919),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF343434),
                modifier = Modifier.size(24.dp),
            )
        }

        if (!feeDescription.isNullOrBlank()) {
            Text(
                text = feeDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                style = AppTheme.typography.bodyMedium,
                color = Color(0xFF6A6A6A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        HorizontalDivider(color = Color(0xFFEAEAEA))
    }
}

@Composable
private fun SwipeRevealFeeRow(
    content: @Composable () -> Unit,
) {
    val deleteWidth = 84.dp
    val density = LocalDensity.current
    val maxSwipe = with(density) { -deleteWidth.toPx() }
    val swipeThreshold = maxSwipe / 2f
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds(),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFFFF5A52)),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Box(
                modifier = Modifier
                    .width(deleteWidth)
                    .fillMaxHeight()
                    .clickable { },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .fillMaxWidth()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(maxSwipe, 0f)
                        },
                        onDragEnd = {
                            offsetX = if (offsetX > swipeThreshold) 0f else maxSwipe
                        },
                    )
                },
        ) {
            content()
        }
    }
}

@Composable
private fun PaymentStatusIcon(
    paymentStatus: Int?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 18.dp,
) {
    val statusUi = paymentStatus.toPaymentStatusUi()

    Icon(
        imageVector = statusUi.icon,
        contentDescription = null,
        tint = statusUi.tint,
        modifier = modifier.size(iconSize),
    )
}

@Composable
private fun FeeTotalBar(
    totalText: String,
    onClick: () -> Unit,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Filled.KeyboardArrowUp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(Color(0xFFF3F3F3))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(resourcesR.string.paymentdetail_total),
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.heading05,
            color = Color(0xFF181818),
        )

        Text(
            text = totalText,
            style = AppTheme.typography.heading05,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF181818),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            imageVector = trailingIcon,
            contentDescription = null,
            tint = Color(0xFF303030),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeeTotalBottomSheet(
    completedTotalText: String,
    pendingTotalText: String,
    overdueTotalText: String,
    totalText: String,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                FeeTotalSheetRow(
                    icon = Icons.Filled.CheckCircle,
                    iconTint = Color(0xFF557D31),
                    title = stringResource(resourcesR.string.paymentdetail_completed_payment),
                    amount = completedTotalText,
                )
                FeeTotalSheetRow(
                    icon = Icons.Filled.Warning,
                    iconTint = Color(0xFF9B9B9B),
                    title = stringResource(resourcesR.string.paymentdetail_pending_payment),
                    amount = pendingTotalText,
                )
                FeeTotalSheetRow(
                    icon = Icons.Filled.Warning,
                    iconTint = Color(0xFFE95B50),
                    title = stringResource(resourcesR.string.paymentdetail_overdue_payment),
                    amount = overdueTotalText,
                )
            }

            HorizontalDivider(color = Color(0xFFE8E8E8))

            FeeTotalBar(
                totalText = totalText,
                onClick = onDismissRequest,
                trailingIcon = Icons.Filled.KeyboardArrowDown,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun FeeTotalSheetRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    amount: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.heading06,
            color = Color(0xFF171717),
        )

        Text(
            text = amount,
            style = AppTheme.typography.heading06,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF171717),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeeStatusBottomSheet(
    fee: FeeDetailItemDomainModel,
    onReminderClick: () -> Unit,
    onMarkAsPaidClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },
    )
    val isOverdue = fee.paymentStatus == 2
    val isPending = fee.paymentStatus != 1 && fee.paymentStatus != 2
    val isActionRequired = isPending || isOverdue
    val statusUi = fee.paymentStatus.toPaymentStatusUi()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
        ) {
            Text(
                text = stringResource(resourcesR.string.paymentdetail_status_sheet_title),
                modifier = Modifier.fillMaxWidth(),
                style = AppTheme.typography.heading04,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF171717),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(28.dp))

            FeeStatusDetailRow(
                label = stringResource(resourcesR.string.paymentdetail_status_label),
                value = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PaymentStatusIcon(
                            paymentStatus = fee.paymentStatus,
                            iconSize = 22.dp,
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = statusUi.label,
                            style = AppTheme.typography.heading06,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF171717),
                        )
                    }
                },
            )

            FeeStatusDetailRow(
                label = stringResource(resourcesR.string.paymentdetail_category_label),
                valueText = fee.categoryName.orEmpty().ifBlank { "-" },
            )

            FeeStatusDetailRow(
                label = stringResource(
                    if (isActionRequired) {
                        resourcesR.string.paymentdetail_due_date_label
                    } else {
                        resourcesR.string.paymentdetail_payment_date_label
                    },
                ),
                valueText = fee.paymentDate.formatDisplayDate(),
            )

            FeeStatusDetailRow(
                label = stringResource(
                    if (isActionRequired) {
                        resourcesR.string.paymentdetail_amount_due_label
                    } else {
                        resourcesR.string.paymentdetail_paid_amount_label
                    },
                ),
                valueText = fee.balanceText ?: fee.balance.toTryAmountText(),
                emphasize = true,
            )

            if (isActionRequired) {
                Spacer(modifier = Modifier.height(32.dp))

                ReminderActionCard(
                    text = stringResource(
                        if (isOverdue) {
                            resourcesR.string.paymentdetail_overdue_reminder_action
                        } else {
                            resourcesR.string.paymentdetail_pending_reminder_action
                        },
                    ),
                    onClick = onReminderClick,
                )

                Spacer(modifier = Modifier.height(24.dp))

                AppButton.PrimaryLarge(
                    text = stringResource(resourcesR.string.paymentdetail_mark_completed_action),
                    onClick = onMarkAsPaidClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ReminderActionCard(
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE2E2E2),
                shape = RoundedCornerShape(16.dp),
            )
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = resourcesR.drawable.img_notification_with_badge),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(26.dp),
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.heading06,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF171717),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF2D2D2D),
        )
    }
}

@Composable
private fun FeeStatusDetailRow(
    label: String,
    valueText: String? = null,
    emphasize: Boolean = false,
    showDivider: Boolean = true,
    value: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.heading06,
                color = Color(0xFF202020),
            )

            Text(
                text = ":",
                modifier = Modifier.padding(horizontal = 10.dp),
                style = AppTheme.typography.heading06,
                color = Color(0xFF202020),
            )

            Box(
                modifier = Modifier.weight(1.7f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value != null) {
                    value()
                } else {
                    Text(
                        text = valueText.orEmpty(),
                        style = AppTheme.typography.heading06,
                        fontWeight = if (emphasize) FontWeight.Bold else FontWeight.SemiBold,
                        color = Color(0xFF171717),
                    )
                }
            }
        }

        if (showDivider) {
            HorizontalDivider(color = Color(0xFFE8E8E8))
        }
    }
}

private data class PaymentStatusUi(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val tint: Color,
    val label: String,
)

@Composable
private fun Int?.toPaymentStatusUi(): PaymentStatusUi =
    when (this) {
        1 -> PaymentStatusUi(
            icon = Icons.Filled.CheckCircle,
            tint = Color(0xFF557D31),
            label = stringResource(resourcesR.string.paymentdetail_status_completed),
        )

        2 -> PaymentStatusUi(
            icon = Icons.Filled.Warning,
            tint = Color(0xFFE95B50),
            label = stringResource(resourcesR.string.paymentdetail_status_overdue),
        )

        else -> PaymentStatusUi(
            icon = Icons.Filled.Warning,
            tint = Color(0xFF9B9B9B),
            label = stringResource(resourcesR.string.paymentdetail_status_pending),
        )
    }

@Composable
private fun FeeEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F3F3)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = Color(0xFF7A7A7A),
                modifier = Modifier.size(34.dp),
            )
        }

        Text(
            text = stringResource(resourcesR.string.paymentdetail_empty),
            style = AppTheme.typography.heading05,
            textAlign = TextAlign.Center,
            color = Color(0xFF6A6A6A),
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun FeeDetailAvatar(
    imageUrl: String?,
    fallbackText: String,
) {
    val initials = remember(fallbackText) {
        fallbackText
            .split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
    }

    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(CircleShape)
            .background(Color(0xFFF0F0F0)),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AppAsyncImageLoader.Load(
                data = imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = initials.ifBlank { "?" },
                style = AppTheme.typography.subtitleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A5A5A),
            )
        }
    }
}

private fun String?.formatDisplayDate(): String {
    if (this.isNullOrBlank()) return "-"

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("tr"))

    return runCatching { OffsetDateTime.parse(this).format(formatter) }
        .recoverCatching { Instant.parse(this).atOffset(ZoneOffset.UTC).format(formatter) }
        .recoverCatching { LocalDate.parse(this).format(formatter) }
        .getOrElse { this }
}

private fun Double?.toTryAmountText(): String {
    if (this == null) return "-"

    val numberText = if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        this.toString()
    }

    return "$numberText TL"
}
