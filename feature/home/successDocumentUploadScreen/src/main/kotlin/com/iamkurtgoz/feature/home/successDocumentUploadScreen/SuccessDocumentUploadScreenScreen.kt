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
package com.iamkurtgoz.feature.home.successDocumentUploadScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSuccessDocumentUploadRoute
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.domain.dataStore.CustomUserRole

@Composable
internal fun SuccessDocumentUploadScreenScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    viewModel: SuccessDocumentUploadScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SuccessDocumentUploadScreenScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SuccessDocumentUploadScreenScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SuccessDocumentUploadScreenScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SuccessDocumentUploadScreenScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SuccessDocumentUploadScreenScreenContract.SideEffect.NavigateToTrainingScreen -> navigateToTrainingScreen(event.model)
            is SuccessDocumentUploadScreenScreenContract.SideEffect.NavigateToHome -> navigateToHome()
        }
    }

    SuccessDocumentUploadScreenScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SuccessDocumentUploadScreenScreenScaffold(
    state: SuccessDocumentUploadScreenScreenContract.State,
    setEvent: (SuccessDocumentUploadScreenScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            val title = when (state.customUserRole) {
                CustomUserRole.TRAINER, CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> {
                    "Antrenman Grubunu Oluştur"// TODO: Localize
                }
                CustomUserRole.CLUB_OFFICIAL -> {
                    "Anasayfa"// TODO: Localize
                }
                else -> {
                    "Anasayfa"// TODO: Localize
                }
            }
            AppButton.PrimaryLarge(
                text = title,
                onClick = {
                    when (state.customUserRole) {
                        CustomUserRole.TRAINER, CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> {
                            setEvent.invoke(SuccessDocumentUploadScreenScreenContract.Event.NavigateToTrainingScreen)
                        }
                        CustomUserRole.CLUB_OFFICIAL -> {
                            setEvent.invoke(SuccessDocumentUploadScreenScreenContract.Event.NavigateToHome)
                        }
                        else -> {
                            setEvent.invoke(SuccessDocumentUploadScreenScreenContract.Event.NavigateToHome)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingHuge)
                    .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                    .padding(bottom = AppTheme.spacing.spacingHuge),
            )
        },
    ) { padding ->
        SuccessDocumentUploadScreenScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SuccessDocumentUploadScreenScreenContract.Event.DismissDialogs)
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
            SuccessDocumentUploadScreenScreenScaffold(
                state = SuccessDocumentUploadScreenScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSuccessDocumentUploadRoute(
                        model = HomeScreenSuccessDocumentUploadScreenNavigateModel(
                            clubId = null,
                            founderUserId = null,
                            clubName = null,
                            logo = null,
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
