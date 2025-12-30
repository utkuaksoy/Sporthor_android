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

import androidx.compose.runtime.Immutable
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.DocumentUploadSectionItem
import com.iamkurtgoz.core.navigation.HomeScreenSendClubAuthDocumentRoute
import com.iamkurtgoz.core.navigation.model.home.successDocumentUploadScreen.HomeScreenSuccessDocumentUploadScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.trainingScreen.HomeScreenTrainingScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.dataStore.CustomUserRole
import com.iamkurtgoz.domain.model.base.AlertDialogModel

internal class SendClubAuthDocumentScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val route: HomeScreenSendClubAuthDocumentRoute,
        val documentList: List<DocumentUploadSectionItem> = listOf(
            DocumentUploadSectionItem(
                index = AppDefaults.ZERO,
                file = null,
            ),
        ),
        val selectedDocumentIndex: Int? = null,
        val customUserRole: CustomUserRole = CustomUserRole.OTHER,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
        data object NavigateToHome : SideEffect()
        data class NavigateToTrainingScreen(val model: HomeScreenTrainingScreenNavigateModel) : SideEffect()
        data class NavigateToSuccessDocumentUploadScreen(val model: HomeScreenSuccessDocumentUploadScreenNavigateModel) : SideEffect()
        data class NavigateToWebView(val routeType: HomeScreenWebViewScreenNavigateModel) : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object AddNewDocumentClick : Event()
        data class OnSelectDocumentClick(val index: Int) : Event()
        data class OnDocumentPicked(val path: String?, val extension: String?) : Event()
        data object NavigateToHome : Event()
        data object NavigateToTrainingScreen : Event()
        data object UploadRequest : Event()
        data class NavigateToWebView(val routeType: HomeScreenWebViewScreenNavigateModel) : Event()
    }

    object Static
}
