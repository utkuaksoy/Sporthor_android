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

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.os.Build
import android.os.CountDownTimer
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.domain.core.CoreViewModel
import com.iamkurtgoz.domain.eventbus.AppEventBus
import com.iamkurtgoz.domain.model.response.LocalMediaDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class CameraxViewModel @Inject constructor(
    @ApplicationContext private val application: Application,
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    private val appEventBus: AppEventBus,
) : CoreViewModel<CameraxScreenContract.State, CameraxScreenContract.SideEffect, CameraxScreenContract.Event>(
    initialState = CameraxScreenContract.State(
        isLoading = false,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
    ),
) {
    private var recording: Recording? = null
    private var recordingTimer: CountDownTimer? = null

    override fun setEvent(event: CameraxScreenContract.Event) {
        when (event) {
            is CameraxScreenContract.Event.Initialize -> handleOneTimeEvent(event, ::initialize)
            is CameraxScreenContract.Event.NavigateUp -> setSideEffect(CameraxScreenContract.SideEffect.NavigateUp)
            is CameraxScreenContract.Event.PopBackStack -> setSideEffect(CameraxScreenContract.SideEffect.PopBackStack)
            is CameraxScreenContract.Event.DismissDialogs -> dismissDialogs()
            is CameraxScreenContract.Event.ToggleCamera -> toggleCamera()
            is CameraxScreenContract.Event.StartVideoRecording -> startVideoRecording()
            is CameraxScreenContract.Event.StopVideoRecording -> stopVideoRecording()
            is CameraxScreenContract.Event.TakePhoto -> takePhoto()
            is CameraxScreenContract.Event.SetUpCamera -> setupCamera(event.lifecycleOwner, event.cameraProvider, event.preview)
        }
    }

    // Events functions
    private fun initialize() = viewModelScope.launch { }

    private fun dismissDialogs() {
        updateState { it.copy(alertDialogModel = null) }
    }

    private fun toggleCamera() {
        updateState { state ->
            state.copy(
                cameraSelector = if (viewState.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                },
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun startVideoRecording() {
        val videoCapture = viewState.videoCapture ?: return
        stopVideoRecording()
        updateState { state ->
            state.copy(
                isRecording = true,
            )
        }

        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Sporthor")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(application.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(application, mediaStoreOutputOptions)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(application)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        startRecordingTimer()
                    }

                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val uri = recordEvent.outputResults.outputUri
                            val localMediaDomainModel = LocalMediaDomainModel(
                                id = contentValues.getAsLong(MediaStore.MediaColumns._ID),
                                mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                                name = name,
                                dateAdded = System.currentTimeMillis() / 1000, // Convert to seconds as MediaStore typically uses
                                uri = uri,
                                mimeType = "video/mp4",
                                size = null,
                                duration = null,
                                directory = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "Movies/Sporthor" else null,
                                filepath = uri.path,
                            )

                            updateState { state ->
                                state.copy(
                                    lastCapturedUri = uri,
                                    localMediaDomainModel = localMediaDomainModel,
                                )
                            }
                            viewModelScope.launch {
                                appEventBus.mediaFileSaved(
                                    localMediaDomainModel = localMediaDomainModel,
                                )
                                setSideEffect(CameraxScreenContract.SideEffect.PopBackStack)
                            }
                        } else {
                            recording?.close()
                            recording = null
                            Timber.tag("CameraViewModel").e("Video kayıt hatası: ${recordEvent.error}")
                        }
                        updateState { state ->
                            state.copy(
                                isRecording = false,
                            )
                        }
                    }
                }
            }
    }

    private fun startRecordingTimer() {
        recordingTimer = object : CountDownTimer(CameraxScreenContract.Static.MILLIS_IN_FUTURE, CameraxScreenContract.Static.COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                updateState { state ->
                    state.copy(
                        recordingTimeLeft = millisUntilFinished / CameraxScreenContract.Static.COUNT_DOWN_INTERVAL,
                    )
                }
            }

            override fun onFinish() {
                stopVideoRecording()
            }
        }.start()
    }

    private fun stopVideoRecording() {
        recordingTimer?.cancel()
        if (viewState.isRecording) {
            recording?.stop()
            recording = null
            updateState { state ->
                state.copy(
                    isRecording = false,
                    recordingTimeLeft = AppDefaults.VIDEO_MAX_DURATION.toLong(),
                )
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = viewState.imageCapture ?: return

        updateState {
            it.copy(
                isRecordButtonEnabled = false,
            )
        }

        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Sporthor")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(application.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(application),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val localMediaDomainModel = LocalMediaDomainModel(
                        id = contentValues.getAsLong(MediaStore.MediaColumns._ID),
                        mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                        name = name,
                        dateAdded = System.currentTimeMillis() / 1000,
                        uri = outputFileResults.savedUri,
                        mimeType = "image/jpeg",
                        size = null,
                        duration = null,
                        directory = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) "Pictures/Sporthor" else null,
                        filepath = outputFileResults.savedUri?.path,
                    )
                    updateState { state ->
                        state.copy(
                            lastCapturedUri = outputFileResults.savedUri,
                            localMediaDomainModel = localMediaDomainModel,
                            isRecordButtonEnabled = true,
                        )
                    }
                    viewModelScope.launch {
                        appEventBus.mediaFileSaved(
                            localMediaDomainModel = localMediaDomainModel,
                        )
                        setSideEffect(CameraxScreenContract.SideEffect.PopBackStack)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Timber.tag("CameraViewModel").e(exception, "Fotoğraf kayıt hatası: ${exception.message}")
                    updateState { state ->
                        state.copy(
                            isRecordButtonEnabled = true,
                        )
                    }
                }
            },
        )
    }

    @Suppress("TooGenericExceptionCaught")
    private fun setupCamera(
        lifecycleOwner: LifecycleOwner,
        cameraProvider: ProcessCameraProvider,
        preview: Preview,
    ) {
        try {
            cameraProvider.unbindAll()
            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            val videoCapture = VideoCapture.withOutput(recorder)
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                viewState.cameraSelector,
                preview,
                imageCapture,
                videoCapture,
            )
            updateState { state ->
                state.copy(
                    imageCapture = imageCapture,
                    videoCapture = videoCapture,
                )
            }
        } catch (e: Exception) {
            Timber.tag("CameraViewModel").e(e, "Kamera başlatma hatası")
        }
    }
}
