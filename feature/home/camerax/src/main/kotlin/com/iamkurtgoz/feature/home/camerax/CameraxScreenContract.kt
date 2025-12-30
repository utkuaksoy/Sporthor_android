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
package com.iamkurtgoz.feature.home.camerax

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.runtime.Immutable
import androidx.lifecycle.LifecycleOwner
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreState
import com.iamkurtgoz.domain.model.base.AlertDialogModel
import com.iamkurtgoz.domain.model.response.LocalMediaDomainModel

internal class CameraxScreenContract {
    @Immutable
    data class State(
        override val isLoading: Boolean,
        val appBuildConfigStatePack: AppBuildConfigStatePack,
        val appRemoteConfigStatePack: AppRemoteConfigStatePack,
        val isShimmerLoading: Boolean = false,
        val alertDialogModel: AlertDialogModel? = null,
        val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
        val isRecordButtonEnabled: Boolean = true,
        val isRecording: Boolean = false,
        val recordingTimeLeft: Long = AppDefaults.VIDEO_MAX_DURATION.toLong(),
        val lastCapturedUri: Uri? = null,
        val localMediaDomainModel: LocalMediaDomainModel? = null,
        val imageCapture: ImageCapture? = null,
        val videoCapture: VideoCapture<Recorder>? = null,
    ) : CoreState.ViewState

    sealed class SideEffect : CoreState.SideEffect {
        data object NavigateUp : SideEffect()
        data object PopBackStack : SideEffect()
    }

    sealed class Event : CoreState.Event {
        data object Initialize : Event()
        data object NavigateUp : Event()
        data object PopBackStack : Event()
        data object DismissDialogs : Event()
        data object ToggleCamera : Event()
        data object StartVideoRecording : Event()
        data object StopVideoRecording : Event()
        data object TakePhoto : Event()
        data class SetUpCamera(val lifecycleOwner: LifecycleOwner, val cameraProvider: ProcessCameraProvider, val preview: Preview) : Event()
    }

    object Static {
        const val MILLIS_IN_FUTURE: Long = 30000
        const val COUNT_DOWN_INTERVAL: Long = 1000
    }
}
