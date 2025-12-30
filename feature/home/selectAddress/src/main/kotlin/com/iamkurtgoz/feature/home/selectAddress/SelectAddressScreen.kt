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
package com.iamkurtgoz.feature.home.selectAddress

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.circlebutton.AppCircleButton
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSelectAddressRoute
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SelectAddressScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: SelectAddressViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SelectAddressScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SelectAddressScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SelectAddressScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SelectAddressScreenContract.SideEffect.PopBackStack -> popBackStack()
        }
    }

    SelectAddressScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SelectAddressScreenScaffold(
    state: SelectAddressScreenContract.State,
    setEvent: (SelectAddressScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingHuge),
            ) {
                AppCircleButton.SecondaryGrayLarge(
                    icon = resourcesR.drawable.img_back_arrow,
                    onClick = {
                        setEvent.invoke(SelectAddressScreenContract.Event.NavigateUp)
                    },
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingHuge)
                        .padding(end = AppTheme.spacing.spacingSmallest),
                )

                AppTextField.SearchField(
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmallest)
                        .padding(end = AppTheme.spacing.spacingHuge),
                    placeholder = "Ara", // TODO: Localize
                    value = state.textSearch.value,
                    trailingIcon = if (state.textSearch.value.isNotEmpty()) resourcesR.drawable.img_close_circle else null,
                    trailingIconClick = {
                        setEvent.invoke(SelectAddressScreenContract.Event.SetTextSearch(""))
                    },
                    leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                    onValueChange = {
                        setEvent.invoke(SelectAddressScreenContract.Event.SetTextSearch(it))
                    },
                )
            }
        },
        bottomBar = {
            if (state.route.isMapActive && state.textSearch.value.isEmpty() && state.userLocation != null) {
                AppButton.PrimaryLarge(
                    text = "Konumu Al", // TODO: Localize,
                    onClick = {
                        setEvent.invoke(SelectAddressScreenContract.Event.FindSelectedAddress)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingHuge)
                        .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding()),
                )
            }
        }
    ) { padding ->
        SelectAddressScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SelectAddressScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SelectAddressScreenScaffold(
                state = SelectAddressScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSelectAddressRoute(
                        isMapActive = true,
                        isBlackBackground = false,
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
