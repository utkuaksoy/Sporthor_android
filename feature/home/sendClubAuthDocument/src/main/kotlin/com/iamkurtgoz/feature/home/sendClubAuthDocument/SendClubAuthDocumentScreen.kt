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
package com.iamkurtgoz.feature.home.sendClubAuthDocument

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
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
import com.iamkurtgoz.core.commonui.component.DocumentPicker
import com.iamkurtgoz.core.commonui.component.log.TrackedScreen
import com.iamkurtgoz.core.commonui.extension.Alert
import com.iamkurtgoz.core.commonui.extension.observeSideEffect
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingDialog
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenSendClubAuthDocumentRoute
import com.iamkurtgoz.core.navigation.model.home.sendClubAuthDocument.HomeScreenSendClubAuthDocumentScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.domain.dataStore.CustomUserRole
import com.iamkurtgoz.feature.home.sendClubAuthDocument.component.TermsAgreementText

@Composable
internal fun SendClubAuthDocumentScreen(
    navigateUp: () -> Unit,
    popBackStack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToTrainingScreen: (HomeScreenTrainingScreenNavigateModel) -> Unit,
    navigateToSuccessDocumentUploadScreen: (HomeScreenSuccessDocumentUploadScreenNavigateModel) -> Unit,
    viewModel: SendClubAuthDocumentViewModel = hiltViewModel(),
    navigateToWebView: (routeType: HomeScreenWebViewScreenNavigateModel) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackedScreen("SendClubAuthDocumentScreen")

    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(SendClubAuthDocumentScreenContract.Event.Initialize)
    }

    viewModel.sideEffect.observeSideEffect { event ->
        when (event) {
            is SendClubAuthDocumentScreenContract.SideEffect.NavigateUp -> navigateUp()
            is SendClubAuthDocumentScreenContract.SideEffect.PopBackStack -> popBackStack()
            is SendClubAuthDocumentScreenContract.SideEffect.NavigateToHome -> navigateToHome.invoke()
            is SendClubAuthDocumentScreenContract.SideEffect.NavigateToTrainingScreen -> navigateToTrainingScreen.invoke(event.model)
            is SendClubAuthDocumentScreenContract.SideEffect.NavigateToSuccessDocumentUploadScreen -> navigateToSuccessDocumentUploadScreen(event.model)
            is SendClubAuthDocumentScreenContract.SideEffect.NavigateToWebView -> navigateToWebView(event.routeType)
        }
    }

    SendClubAuthDocumentScreenScaffold(
        state = state,
        setEvent = viewModel::setEvent,
    )
}

@Composable
private fun SendClubAuthDocumentScreenScaffold(
    state: SendClubAuthDocumentScreenContract.State,
    setEvent: (SendClubAuthDocumentScreenContract.Event) -> Unit,
) {
    AppThemeScaffold(
        bottomBar = {
            Column {
                TermsAgreementText(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.spacing.spacingHuge),
                    onTermsClick = {
                        setEvent(
                            SendClubAuthDocumentScreenContract.Event.NavigateToWebView(
                                routeType = HomeScreenWebViewScreenNavigateModel(
                                    title = "Kullanım Şartları",
                                    url = "https://accounts.sporthor.com/Agreement/TermsofUse",
                                ),
                            ),
                        )
                    },
                    onPrivacyClick = {
                        setEvent(
                            SendClubAuthDocumentScreenContract.Event.NavigateToWebView(
                                routeType = HomeScreenWebViewScreenNavigateModel(
                                    title = "Gizlilik Sözleşmesi",
                                    url = "https://accounts.sporthor.com/Agreement/Privacy",
                                ),
                            ),
                        )
                    },
                )

                AppButton.PrimaryLarge(
                    text = "Gönder", // TODO: Localize
                    onClick = {
                        setEvent.invoke(SendClubAuthDocumentScreenContract.Event.UploadRequest)
                    },
                    enabled = state.documentList.any { it.file != null },
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingHuge)
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                )

                AppButton.OutlineLarge(
                    text = "Bu Adımı Atla", // TODO: Localize
                    onClick = {
                        if (state.route.fromGenerateClub) {
                            setEvent.invoke(SendClubAuthDocumentScreenContract.Event.NavigateToHome)
                        } else {
                            when (state.customUserRole) {
                                CustomUserRole.TRAINER, CustomUserRole.CLUB_OFFICIAL_AND_TRAINER -> {
                                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.NavigateToTrainingScreen)
                                }
                                CustomUserRole.CLUB_OFFICIAL -> {
                                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.NavigateToHome)
                                }
                                else -> {
                                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.NavigateToHome)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = AppTheme.spacing.spacingRegular)
                        .padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
                        .padding(bottom = AppTheme.spacing.spacingHuge)
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.spacingMedium),
                )
            }
        },
    ) { padding ->
        SendClubAuthDocumentScreenContent(
            modifier = Modifier
                .padding(padding),
            state = state,
            setEvent = setEvent,
        )

        state.alertDialogModel?.Alert {
            setEvent.invoke(SendClubAuthDocumentScreenContract.Event.DismissDialogs)
        }

        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppLoadingDialog()
        }

        if (state.selectedDocumentIndex != null) {
            DocumentPicker(
                onResult = { path: String?, extension: String? ->
                    setEvent.invoke(SendClubAuthDocumentScreenContract.Event.OnDocumentPicked(path, extension))
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
            SendClubAuthDocumentScreenScaffold(
                state = SendClubAuthDocumentScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenSendClubAuthDocumentRoute(
                        model = HomeScreenSendClubAuthDocumentScreenNavigateModel(
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
