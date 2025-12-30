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
package com.iamkurtgoz.core.commonui.component.fileSelectDialog

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.helper.FileHelper
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.resources.R as resourcesR

private object FileSelectDialogStatic {
    const val GALLERY_SELECT_INPUT = "image/*"
    const val VIDEO_SELECT_INPUT = "video/*"
    const val DOCUMENT_SELECT_INPUT = "*/*"
}

enum class FileSelectDialogFileType {
    IMAGE,
    VIDEO,
    DOCUMENT,
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FileSelectDialog(
    modifier: Modifier = Modifier,
    onSelectedFileCallback: (path: String?, extension: String?, FileSelectDialogFileType?) -> Unit = { path, extension, type -> },
) {
    // Permission states
    val storagePermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            android.Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        rememberPermissionState(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }

    val videoPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            android.Manifest.permission.READ_MEDIA_VIDEO,
        )
    } else {
        rememberPermissionState(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA,
    )

    val recordAudioPermissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO,
    )

    // State
    val context: Context = LocalContext.current
    var state by rememberSaveable(stateSaver = FileSelectDialogState.Saver) {
        mutableStateOf(FileSelectDialogState())
    }

    // Launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val realPath: String? = FileHelper.getRealPathFromURI(context, uri)
                val extension = FileHelper.getFileExtension(realPath ?: "")
                onSelectedFileCallback.invoke(realPath, extension, state.lastSelectedFileType)
            }
        },
    )

    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val realPath: String? = FileHelper.getRealPathFromURI(context, uri)
                val extension = FileHelper.getFileExtension(realPath ?: "")
                onSelectedFileCallback.invoke(realPath, extension, state.lastSelectedFileType)
            }
        },
    )

    val imageCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success) {
            val realPath: String? = state.tempFile?.absolutePath
            val extension = FileHelper.getFileExtension(realPath ?: "")
            onSelectedFileCallback.invoke(realPath, extension, state.lastSelectedFileType)
        }
    }

    val videoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
    ) { success: Boolean ->
        if (success) {
            val realPath: String? = state.tempFile?.absolutePath
            val extension = FileHelper.getFileExtension(realPath ?: "")
            onSelectedFileCallback.invoke(realPath, extension, state.lastSelectedFileType)
        }
    }

    // Permission Checkers
    LaunchedEffect(storagePermissionState.status) {
        if (storagePermissionState.status.isGranted && state.shouldOpenGalleryWhenAllowPermission) {
            state = state.copy(shouldOpenGalleryWhenAllowPermission = false)
            galleryLauncher.launch(FileSelectDialogStatic.GALLERY_SELECT_INPUT)
        }
    }

    LaunchedEffect(videoPermissionState.status) {
        if (videoPermissionState.status.isGranted && state.shouldOpenDocumentPickerWhenAllowPermission) {
            state = state.copy(shouldOpenDocumentPickerWhenAllowPermission = false)
            when (state.lastSelectedFileType) {
                FileSelectDialogFileType.VIDEO -> galleryLauncher.launch(FileSelectDialogStatic.VIDEO_SELECT_INPUT)
                FileSelectDialogFileType.DOCUMENT -> documentLauncher.launch(FileSelectDialogStatic.DOCUMENT_SELECT_INPUT)
                else -> Unit
            }
        }
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status.isGranted) {
            if (state.shouldOpenCameraWhenAllowPermission) {
                state = state.createTempFile(context = context, fileType = FileSelectDialogFileType.IMAGE)
                state = state.copy(shouldOpenCameraWhenAllowPermission = false)
                state.tempFileUri?.let { uri ->
                    imageCameraLauncher.launch(uri)
                }
            } else if (state.shouldOpenVideoCameraWhenAllowPermission && recordAudioPermissionState.status.isGranted) {
                state = state.createTempFile(context = context, fileType = FileSelectDialogFileType.VIDEO)
                state = state.copy(shouldOpenVideoCameraWhenAllowPermission = false)
                state.tempFileUri?.let { uri ->
                    videoCameraLauncher.launch(uri)
                }
            }
        }
    }

    LaunchedEffect(recordAudioPermissionState.status) {
        if (recordAudioPermissionState.status.isGranted &&
            cameraPermissionState.status.isGranted &&
            state.shouldOpenVideoCameraWhenAllowPermission
        ) {
            state = state.createTempFile(context = context, fileType = FileSelectDialogFileType.VIDEO)
            state = state.copy(shouldOpenVideoCameraWhenAllowPermission = false)
            state.tempFileUri?.let { uri ->
                videoCameraLauncher.launch(uri)
            }
        }
    }

    var isSelectedCamera: Boolean by rememberSaveable { mutableStateOf(false) }
    val density: Density = LocalDensity.current
    var dynamicHeight by remember { mutableStateOf(AppDefaults.ZERO.dp) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.generalColors.backgroundPrimary)
            .heightIn(min = AppTheme.dimens.dp200),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(top = AppTheme.spacing.spacingSmall)
                .onGloballyPositioned {
                    dynamicHeight = with(density) {
                        it.size.height.toDp()
                    }
                },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val itemWidth = ((AppTheme.configuration.getScreenWidthDp() - (AppTheme.spacing.spacingMedium * AppDefaults.TWO)) / AppDefaults.THREE) - (AppTheme.spacing.spacingSmallest * AppDefaults.TWO)

            FileSelectDialogCard(
                modifier = Modifier
                    .width(itemWidth)
                    .aspectRatio(AppDefaults.ASPECT_RATIO_1_25),
                icon = resourcesR.drawable.img_camera,
                title = "Kamera", // TODO: Localize
                isSelected = isSelectedCamera,
                onClick = {
                    isSelectedCamera = true
                },
            )

            FileSelectDialogCard(
                modifier = Modifier
                    .width(itemWidth)
                    .aspectRatio(AppDefaults.ASPECT_RATIO_1_25),
                icon = resourcesR.drawable.img_image_version_three,
                title = "Fotoğraflar", // TODO: Localize
                isSelected = false,
                onClick = {
                    isSelectedCamera = false
                    state = state.copy(
                        lastSelectedFileType = FileSelectDialogFileType.IMAGE,
                    )
                    if (storagePermissionState.status.isGranted) {
                        galleryLauncher.launch(FileSelectDialogStatic.GALLERY_SELECT_INPUT)
                    } else {
                        state = state.copy(
                            shouldOpenGalleryWhenAllowPermission = true,
                        )
                        storagePermissionState.launchPermissionRequest()
                    }
                },
            )

            FileSelectDialogCard(
                modifier = Modifier
                    .width(itemWidth)
                    .aspectRatio(AppDefaults.ASPECT_RATIO_1_25),
                icon = resourcesR.drawable.img_document,
                title = "Belge", // TODO: Localize
                isSelected = false,
                onClick = {
                    isSelectedCamera = false
                    state = state.copy(
                        lastSelectedFileType = FileSelectDialogFileType.DOCUMENT,
                    )
                    if (storagePermissionState.status.isGranted) {
                        documentLauncher.launch(FileSelectDialogStatic.DOCUMENT_SELECT_INPUT)
                    } else {
                        state = state.copy(
                            shouldOpenDocumentPickerWhenAllowPermission = true,
                        )
                        storagePermissionState.launchPermissionRequest()
                    }
                },
            )
        }

        if (isSelectedCamera) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(top = AppTheme.spacing.spacingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val itemWidth = ((AppTheme.configuration.getScreenWidthDp() - (AppTheme.spacing.spacingMedium * AppDefaults.TWO)) / AppDefaults.TWO) - (AppTheme.spacing.spacingSmallest)

                FileSelectDialogCard(
                    modifier = Modifier
                        .width(itemWidth)
                        .height(dynamicHeight),
                    icon = resourcesR.drawable.img_camera,
                    title = "Fotoğraf", // TODO: Localize
                    isSelected = false,
                    onClick = {
                        state = state.copy(
                            lastSelectedFileType = FileSelectDialogFileType.IMAGE,
                        )
                        if (cameraPermissionState.status.isGranted) {
                            state = state.createTempFile(context = context, fileType = FileSelectDialogFileType.IMAGE)
                            state.tempFileUri?.let { uri ->
                                imageCameraLauncher.launch(uri)
                            }
                        } else {
                            state = state.copy(shouldOpenCameraWhenAllowPermission = true)
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                )

                FileSelectDialogCard(
                    modifier = Modifier
                        .width(itemWidth)
                        .height(dynamicHeight),
                    icon = resourcesR.drawable.img_camera,
                    title = "Video", // TODO: Localize
                    isSelected = false,
                    onClick = {
                        state = state.copy(
                            lastSelectedFileType = FileSelectDialogFileType.VIDEO,
                        )
                        if (cameraPermissionState.status.isGranted && recordAudioPermissionState.status.isGranted) {
                            state = state.createTempFile(context = context, fileType = FileSelectDialogFileType.VIDEO)
                            state.tempFileUri?.let { uri ->
                                videoCameraLauncher.launch(uri)
                            }
                        } else {
                            state = state.copy(shouldOpenVideoCameraWhenAllowPermission = true)
                            if (!cameraPermissionState.status.isGranted) {
                                cameraPermissionState.launchPermissionRequest()
                            } else {
                                recordAudioPermissionState.launchPermissionRequest()
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun FileSelectDialogCard(
    @DrawableRes icon: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(AppTheme.shapes.radiusMedium)
            .clickable(onClick = onClick),
        shape = AppTheme.shapes.radiusMedium,
        color = if (isSelected) AppTheme.colors.generalColors.green100 else AppTheme.colors.generalColors.backgroundWeak100,
        shadowElevation = AppTheme.dimens.dp0dot5,
        border = BorderStroke(
            width = AppTheme.dimens.dp1,
            color = if (isSelected) AppTheme.colors.generalColors.green500 else AppTheme.colors.generalColors.borderSoft200,
        ),
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "icon",
                modifier = Modifier
                    .size(AppTheme.dimens.dp24),
            )

            Text(
                text = title,
                style = AppTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = AppTheme.colors.generalColors.textPrimary,
            )
        }
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            FileSelectDialog()
        }
    }
}
