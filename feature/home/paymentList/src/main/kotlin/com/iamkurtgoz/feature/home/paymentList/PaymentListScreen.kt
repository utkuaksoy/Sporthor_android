package com.iamkurtgoz.feature.home.paymentList

import android.R as androidR
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbar
import com.iamkurtgoz.core.designsystem.component.toolbar.AppToolbarFields
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.eventbus.impl.PaymentListEventBus

@Composable
internal fun PaymentListScreen(
    navigateUp: () -> Unit,
    navigateToAddFee: (String) -> Unit,
    navigateToFeeDetail: (String, String) -> Unit,
    viewModel: PaymentListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setEvent(PaymentListScreenContract.Event.Initialize)
    }

    AppTheme.appEventBus.paymentListEventBus.observeEventBus { event ->
        if (event is PaymentListEventBus.Event.RefreshList) {
            viewModel.setEvent(PaymentListScreenContract.Event.RefreshData)
        }
    }

    viewModel.sideEffect.observeSideEffect { effect ->
        when (effect) {
            PaymentListScreenContract.SideEffect.NavigateUp -> navigateUp()
            is PaymentListScreenContract.SideEffect.NavigateToAddFee -> {
                navigateToAddFee(effect.trainingGroupId)
            }
            is PaymentListScreenContract.SideEffect.NavigateToFeeDetail -> {
                navigateToFeeDetail(effect.userId, effect.trainingGroupId)
            }
        }
    }

    PaymentListScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentListScreenScaffold(
    state: PaymentListScreenContract.State,
    setEvent: (PaymentListScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            AppToolbar.Toolbar(
                leftContent = {
                    AppToolbarFields.NavigateIcon(
                        onClick = { setEvent(PaymentListScreenContract.Event.NavigateUp) },
                    )
                },
                centerContent = {
                    AppToolbarFields.Title(
                        text = stringResource(resourcesR.string.paymentlist_title),
                    )
                },
                rightContent = {
                    AppToolbarFields.ImageIcon(
                        resId = androidR.drawable.ic_input_add,
                        onClick = {
                            setEvent(PaymentListScreenContract.Event.NavigateToAddFee(trainingGroupId = ""))
                        },
                    )
                },
            )
        },
    ) { padding ->
        PaymentListScreenContent(
            modifier = Modifier.padding(padding),
            state = state,
            setEvent = setEvent,
        )
    }
}
