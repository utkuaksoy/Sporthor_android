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
package com.iamkurtgoz.feature.home.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.commonui.state.keyboardVisibility
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.textfield.AppTextField
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

@Composable
internal fun SearchScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToProfile: (String) -> Unit,
    navigateToTeam: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isKeyboardVisible by keyboardVisibility()

    TrackedScreen("SearchScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SearchScreenContract.Event.Initialize)
    }

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
        }
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SearchScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SearchScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SearchScreenContract.SideEffect.ClearFocusAndHideKeyboard -> {
                focusManager.clearFocus(force = true)
                keyboardController?.hide()
            }
            is SearchScreenContract.SideEffect.NavigateToProfile -> navigateToProfile(event.userId)
            is SearchScreenContract.SideEffect.NavigateToTeam -> navigateToTeam(event.teamId)
        }
    }

    SearchScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SearchScreenScaffold(
    state: SearchScreenContract.State,
    setEvent: (SearchScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.configuration.getSafeContentPaddingValues().calculateTopPadding()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                AppTextField.SearchField(
                    modifier = Modifier
                        .onFocusChanged {
                            setEvent.invoke(SearchScreenContract.Event.SetSearchTextFieldFocusedStatus(it.isFocused))
                        }
                        .weight(AppDefaults.WEIGHT_FULL)
                        .padding(
                            PaddingValues(
                                horizontal = AppTheme.dimens.dp16,
                                vertical = AppTheme.dimens.dp8,
                            ),
                        ),
                    placeholder = "Sporthor’da Ara", // TODO: Localize
                    value = state.textSearch.value,
                    trailingIcon = if (state.textSearch.value.isNotEmpty()) resourcesR.drawable.img_close_circle else null,
                    trailingIconClick = {
                        setEvent.invoke(SearchScreenContract.Event.SetSearchText(""))
                    },
                    leadingIcon = resourcesR.drawable.img_home_button_search_un_selected,
                    onValueChange = {
                        setEvent.invoke(SearchScreenContract.Event.SetSearchText(it))
                    },
                )

                if (state.isSearchTextFieldFocused || LocalInspectionMode.current) {
                    TextButton(
                        onClick = {
                            setEvent.invoke(SearchScreenContract.Event.SetSearchText(""))
                            setEvent.invoke(SearchScreenContract.Event.ClearFocusAndHideKeyboard)
                        },
                        content = {
                            Text(
                                text = "İptal", // TODO: Localize
                                style = AppTheme.typography.labelRegular,
                                color = AppTheme.colors.generalColors.textPrimary,
                            )
                        },
                    )
                }
            }
        },
    ) { padding ->
        SearchScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SearchScreenContract.Event.DismissDialogs)
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
            SearchScreenScaffold(
                state = SearchScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                setEvent = { },
            )
        }
    }
}
