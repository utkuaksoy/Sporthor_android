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
package com.iamkurtgoz.core.commonui.component.photoPicker

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.helper.FileHelper
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR

private object PhotoPickerStatic {
    const val GALLERY_SELECT_INPUT = "image/*"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPicker(
    isShow: Boolean,
    onSelectedFileCallback: (String?) -> Unit = { },
    onDismissRequest: () -> Unit = { },
) {
    if (isShow) {
        ModalBottomSheet(
            containerColor = AppTheme.colors.generalColors.backgroundPrimary,
            scrimColor = AppTheme.colors.generalColors.backgroundPrimary.copy(
                alpha = AppDefaults.COMPOSE_COLORS_THREE_QUARTER_ALPHA,
            ),
            onDismissRequest = onDismissRequest,
            content = {
                PhotoPickerContent(
                    onSelectedFileCallback = onSelectedFileCallback,
                )
            },
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhotoPickerContent(
    onSelectedFileCallback: (String?) -> Unit = { },
) {
    val storagePermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_IMAGES)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA,
    )

    // State
    val context: Context = LocalContext.current
    var state by rememberSaveable(stateSaver = PhotoPickerState.Saver) {
        mutableStateOf(PhotoPickerState())
    }

    // Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val realPath: String? = FileHelper.getRealPathFromURI(context, uri)
                onSelectedFileCallback.invoke(realPath)
            }
        },
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success) {
            state.tempImageFile?.absolutePath?.let(onSelectedFileCallback)
        }
    }

    // Permission Checker
    LaunchedEffect(storagePermissionState.status) {
        if (storagePermissionState.status.isGranted && state.shouldOpenGalleryWhenAllowPermission) {
            state = state.copy(shouldOpenGalleryWhenAllowPermission = false)
            galleryLauncher.launch(PhotoPickerStatic.GALLERY_SELECT_INPUT)
        }
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status.isGranted && state.shouldOpenCameraWhenAllowPermission) {
            state = state.createTempImageFile(context = context)
            state = state.copy(shouldOpenCameraWhenAllowPermission = false)
            state.tempImageFileUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        }
    }

    Row {
        AppButton.OutlineLarge(
            text = stringResource(resourcesR.string.button_camera_button_title),
            onClick = {
                if (cameraPermissionState.status.isGranted) {
                    state = state.createTempImageFile(context = context)
                    state = state.copy(shouldOpenCameraWhenAllowPermission = false)
                    state.tempImageFileUri?.let { uri ->
                        cameraLauncher.launch(uri)
                    }
                } else {
                    state = state.copy(shouldOpenCameraWhenAllowPermission = true)
                    cameraPermissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(vertical = AppTheme.spacing.spacingSmall)
                .weight(AppDefaults.WEIGHT_FULL),
        )

        AppButton.OutlineLarge(
            text = stringResource(resourcesR.string.button_gallery_button_title),
            onClick = {
                if (storagePermissionState.status.isGranted) {
                    galleryLauncher.launch(PhotoPickerStatic.GALLERY_SELECT_INPUT)
                } else {
                    state = state.copy(shouldOpenGalleryWhenAllowPermission = true)
                    storagePermissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier
                .padding(horizontal = AppTheme.spacing.spacingMedium)
                .padding(vertical = AppTheme.spacing.spacingSmall)
                .weight(AppDefaults.WEIGHT_FULL),
        )
    }
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            PhotoPickerContent()
        }
    }
}
