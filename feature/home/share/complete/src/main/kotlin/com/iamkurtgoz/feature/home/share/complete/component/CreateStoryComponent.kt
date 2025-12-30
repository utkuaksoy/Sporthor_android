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
package com.iamkurtgoz.feature.home.share.complete.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.extension.observeEventBus
import com.iamkurtgoz.core.commonui.state.keyboardVisibility
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.extension.imeAndStatusBarPadding
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.navigation.HomeScreenShareCompleteRoute
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteScreenNavigateModelMediaItem
import com.iamkurtgoz.core.navigation.model.home.share.complete.HomeScreenShareCompleteShareTypeScreenNavigateModel
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.eventbus.impl.CreateStoryComponent
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.feature.home.share.complete.CompleteScreenContract
import com.iamkurtgoz.feature.home.share.complete.R as shareCompleteR
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.ViewType
import timber.log.Timber
import java.io.File

@SuppressLint("InflateParams", "MissingPermission")
@OptIn(UnstableApi::class)
@Composable
internal fun CreateStoryComponent(
    exoPlayer: ExoPlayer?,
    state: CompleteScreenContract.State,
    setEvent: (CompleteScreenContract.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current
    var imageEditorViewTextEditView by remember { mutableStateOf<Pair<View, Int>?>(null) }
    val photoEditorRef = remember { mutableStateOf<PhotoEditor?>(null) }
    val isKeyboardShow by keyboardVisibility()

    AppTheme.appEventBus.createStoryComponent.observeEventBus { event ->
        when (event) {
            is CreateStoryComponent.Event.SelectedAddressChanged -> {
                addLocationTextWithPin(
                    photoEditorRef = photoEditorRef.value,
                    context = context,
                    text = event.title,
                    textColor = Color.Black.toArgb(),
                )
            }
        }
    }

    AppThemeScaffold(
        containerColor = AppTheme.colors.generalColors.transparent,
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = AppTheme.spacing.spacingSmallest),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {
                        setEvent.invoke(CompleteScreenContract.Event.NavigateUp)
                    },
                    content = {
                        Image(
                            painter = painterResource(resourcesR.drawable.img_close_circle_48),
                            contentDescription = "close",
                            modifier = Modifier
                                .size(AppTheme.dimens.dp48),
                        )
                    },
                )

                Spacer(
                    modifier = Modifier
                        .weight(AppDefaults.WEIGHT_FULL),
                )

                state.navigateRoute.routeType.selectedMediaList.firstOrNull()?.let { media ->
                    if (media.customMediaType == CustomMediaType.IMAGE) {
                        IconButton(
                            onClick = {
                                val event = CompleteScreenContract.Event.SetShowTextInputDialog(
                                    isShowTextInputDialog = "",
                                )
                                setEvent.invoke(event)
                            },
                            content = {
                                Image(
                                    painter = painterResource(resourcesR.drawable.img_edit_text_circle_48),
                                    contentDescription = "close",
                                    modifier = Modifier
                                        .size(AppTheme.dimens.dp48),
                                )
                            },
                        )

                        IconButton(
                            onClick = {
                                val event = CompleteScreenContract.Event.NavigateToSelectAddressScreen
                                setEvent.invoke(event)
                            },
                            content = {
                                Box(
                                    modifier = Modifier
                                        .size(AppTheme.dimens.dp48)
                                        .background(
                                            color = AppTheme.colors.generalColors.foregroundBlack,
                                            shape = AppTheme.shapes.radiusCircle,
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Image(
                                        painter = painterResource(resourcesR.drawable.img_location_pin),
                                        contentDescription = "close",
                                        modifier = Modifier
                                            .size(AppTheme.dimens.dp24),
                                        colorFilter = ColorFilter.tint(AppTheme.colors.generalColors.foregroundWhite),
                                    )
                                }
                            },
                        )
                    }
                }
            }
        },
        bottomBar = {
            AppButton.PrimaryLarge(
                text = "Paylaş", // TODO: Localize
                modifier = Modifier
                    .fillMaxWidth()
                    .imeAndStatusBarPadding(isKeyboardShow = isKeyboardShow)
                    .padding(vertical = AppTheme.spacing.spacingMedium)
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
                onClick = {
                    state.navigateRoute.routeType.selectedMediaList.firstOrNull()?.let { media ->
                        if (media.customMediaType == CustomMediaType.IMAGE) {
                            setEvent(CompleteScreenContract.Event.SetLoadingStatus(true))
                            val fileName = System.currentTimeMillis().toString() + ".jpg"
                            val file = File(context.cacheDir, fileName)
                            photoEditorRef.value?.saveAsFile(
                                imagePath = file.absolutePath,
                                onSaveListener = object : PhotoEditor.OnSaveListener {
                                    override fun onSuccess(imagePath: String) {
                                        setEvent(CompleteScreenContract.Event.ShareStoryImage(file))
                                    }

                                    override fun onFailure(exception: Exception) {
                                        Timber.e(exception)
                                        setEvent(CompleteScreenContract.Event.SetLoadingStatus(false))
                                    }
                                },
                            )
                        } else if (media.customMediaType == CustomMediaType.VIDEO) {
                            setEvent(CompleteScreenContract.Event.ShareStoryVideo)
                        }
                    }
                },
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .aspectRatio(AppDefaults.ASPECT_RATIO_0_56)
                .background(
                    color = AppTheme.colors.generalColors.foregroundDisabled,
                    shape = AppTheme.shapes.radiusTiny,
                )
                .clip(AppTheme.shapes.radiusTiny),
            contentAlignment = Alignment.Center,
        ) {
            state.navigateRoute.routeType.selectedMediaList.firstOrNull()?.let { media ->
                if (media.customMediaType == CustomMediaType.VIDEO) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(AppDefaults.ASPECT_RATIO_0_56)
                                .background(AppTheme.colors.generalColors.foregroundDisabled),
                            factory = { ctx ->
                                PlayerView(ctx).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                    )
                                    useController = false
                                    player = exoPlayer
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                }
                            },
                        )

                        if (exoPlayer?.playbackState == Player.STATE_BUFFERING) {
                            CircularProgressIndicator(
                                color = AppTheme.colors.generalColors.foregroundWhite,
                                modifier = Modifier.size(AppTheme.dimens.dp36),
                            )
                        }
                    }
                } else {
                    AndroidView(
                        factory = { ctx ->
                            val inflater = LayoutInflater.from(ctx)
                            val view = inflater.inflate(shareCompleteR.layout.photo_editor_view, null)
                            val photoEditorView = view.findViewById<PhotoEditorView>(shareCompleteR.id.photoEditorView)

                            val editor = PhotoEditor.Builder(ctx, photoEditorView)
                                .setPinchTextScalable(true)
                                .build()

                            photoEditorRef.value = editor
                            photoEditorView.source.setImageURI(media.uri)
                            photoEditorView.source.scaleType = ImageView.ScaleType.CENTER_CROP
                            editor.setOnPhotoEditorListener(
                                object : OnPhotoEditorListener {
                                    override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {
                                        Timber.d("onAddViewListener")
                                    }

                                    override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {
                                        imageEditorViewTextEditView = Pair(rootView, colorCode)
                                        val event = CompleteScreenContract.Event.SetShowTextInputDialog(
                                            isShowTextInputDialog = text,
                                        )
                                        setEvent.invoke(event)
                                    }

                                    override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {
                                        Timber.d("onRemoveViewListener")
                                    }

                                    override fun onStartViewChangeListener(viewType: ViewType) {
                                        Timber.d("onStartViewChangeListener")
                                    }

                                    override fun onStopViewChangeListener(viewType: ViewType) {
                                        Timber.d("onStopViewChangeListener")
                                    }

                                    override fun onTouchSourceImage(event: MotionEvent) {
                                        Timber.d("onTouchSourceImage")
                                    }
                                },
                            )
                            view
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(AppDefaults.ASPECT_RATIO_0_56),
                    )

                    state.isShowTextInputDialog?.let {
                        TextInputDialog(
                            initialText = it,
                            initialTextColor = imageEditorViewTextEditView?.second,
                            onConfirm = { text, textColor ->
                                imageEditorViewTextEditView?.let { pair ->
                                    imageEditorViewTextEditView = null
                                    photoEditorRef.value?.editText(pair.first, text, textColor)
                                } ?: run {
                                    addTextWithBackground(
                                        photoEditorRef = photoEditorRef.value,
                                        context = context,
                                        text = text,
                                        textColor = textColor,
                                    )
                                }
                                setEvent.invoke(CompleteScreenContract.Event.DismissDialogs)
                            },
                            onDismiss = {
                                setEvent.invoke(CompleteScreenContract.Event.DismissDialogs)
                            },
                        )
                    }
                }
            }
        }
    }
}

private fun addTextWithBackground(photoEditorRef: PhotoEditor?, context: Context, text: String, textColor: Int) {
    val textView = TextView(context).apply {
        this.text = text
        setTextColor(textColor)
        setBackgroundResource(resourcesR.drawable.text_background_rounded)
        setPadding(20, 10, 20, 10)
        gravity = Gravity.CENTER
        textSize = 16f
    }

    textView.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
    )
    textView.layout(0, 0, textView.measuredWidth, textView.measuredHeight)

    val bitmap = createBitmap(textView.measuredWidth, textView.measuredHeight)
    val canvas = Canvas(bitmap)
    textView.draw(canvas)

    photoEditorRef?.addImage(bitmap)
}

private fun addLocationTextWithPin(photoEditorRef: PhotoEditor?, context: Context, text: String, textColor: Int) {
    val locationContainer = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setBackgroundResource(resourcesR.drawable.location_background_rounded)
        setPadding(16, 12, 20, 12)
    }

    val pinIcon = ImageView(context).apply {
        setImageResource(resourcesR.drawable.ic_location_pin) // Sizin pin drawable'ınız
        layoutParams = LinearLayout.LayoutParams(
            dpToPx(context, 20), // 20dp width
            dpToPx(context, 20), // 20dp height
        ).apply {
            setMargins(0, 0, dpToPx(context, 8), 0) // Sağ tarafa 8dp margin
        }
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    val textView = TextView(context).apply {
        this.text = text
        setTextColor(textColor)
        textSize = 14f
        gravity = Gravity.CENTER_VERTICAL
        typeface = Typeface.DEFAULT
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
    }

    locationContainer.addView(pinIcon)
    locationContainer.addView(textView)

    locationContainer.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
    )
    locationContainer.layout(0, 0, locationContainer.measuredWidth, locationContainer.measuredHeight)

    val bitmap = createBitmap(locationContainer.measuredWidth, locationContainer.measuredHeight)
    val canvas = Canvas(bitmap)
    locationContainer.draw(canvas)

    photoEditorRef?.addImage(bitmap)
}

private fun dpToPx(context: Context, dp: Int): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.generalColors.foregroundPrimary)
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(resourcesR.drawable.img_share_background),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        }

        CreateStoryComponent(
            exoPlayer = null,
            state = CompleteScreenContract.State(
                isLoading = true,
                appBuildConfigStatePack = AppBuildConfigStatePack(),
                appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                navigateRoute = HomeScreenShareCompleteRoute(
                    routeType = HomeScreenShareCompleteScreenNavigateModel(
                        shareType = HomeScreenShareCompleteShareTypeScreenNavigateModel.CreateStory,
                        selectedMediaList = listOf(
                            HomeScreenShareCompleteScreenNavigateModelMediaItem(
                                customMediaType = CustomMediaType.IMAGE,
                                uri = Uri.EMPTY,
                            ),
                        ),
                    ),
                ),
            ),
            setEvent = { },
        )
    }
}
