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
package com.iamkurtgoz.core.commonui.component.post

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.commonui.component.user.UserImageView
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.extension.noRippleClickable
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R as resourcesR
import com.iamkurtgoz.domain.model.enums.CustomMediaType
import com.iamkurtgoz.fake.model.response.FakeMockDashboard
import java.util.UUID

object PostView {
    internal const val LIKED_USER_IMAGE_DATA_LIST_MAX_SIZE = 3
    internal const val LIKED_USER_IMAGE_DATA_LIST_MIN_SIZE = 3
    internal const val LIKED_USER_IMAGE_PADDING_VALUE = 12

    @Composable
    fun Primary(
        postId: String?,
        isOwnPost: Boolean,
        userImageData: Any?,
        userName: String?,
        postData: List<Pair<Any?, Int>>,
        postRatio: Float?,
        isLiked: Boolean,
        likedCount: Int?,
        commentCount: Int?,
        likedUserImageDataList: List<Any?>,
        likedUserNameList: List<String>,
        commentPreviewList: List<Pair<String, String>>,
        time: String?,
        playingVideoUrl: String?,
        setPlayingVideoUrl: (String?) -> Unit,
        setLike: () -> Unit,
        setUnLike: () -> Unit,
        onLongClickImage: (url: String?) -> Unit,
        onLongClickVideo: (url: String?) -> Unit,
        onCommentClick: (postId: String?) -> Unit,
        onComplainPost: () -> Unit,
        onHidePost: () -> Unit,
        onDeletePost: () -> Unit,
        modifier: Modifier = Modifier,
    ) = PostViewImpl(
        postId = postId,
        isOwnPost = isOwnPost,
        userImageData = userImageData,
        userName = userName,
        postData = postData,
        postRatio = postRatio,
        isLiked = isLiked,
        likedCount = likedCount,
        commentCount = commentCount,
        likedUserImageDataList = likedUserImageDataList,
        likedUserNameList = likedUserNameList,
        commentPreviewList = commentPreviewList,
        time = time,
        playingVideoUrl = playingVideoUrl,
        setPlayingVideoUrl = setPlayingVideoUrl,
        setLike = setLike,
        setUnLike = setUnLike,
        onLongClickImage = onLongClickImage,
        onLongClickVideo = onLongClickVideo,
        onCommentClick = onCommentClick,
        onComplainPost = onComplainPost,
        onHidePost = onHidePost,
        onDeletePost = onDeletePost,
        modifier = modifier,
    )
}

@OptIn(UnstableApi::class)
@Composable
private fun PostViewImpl(
    postId: String?,
    isOwnPost: Boolean,
    userImageData: Any?,
    userName: String?,
    postData: List<Pair<Any?, Int>>,
    postRatio: Float?,
    isLiked: Boolean,
    likedCount: Int?,
    commentCount: Int?,
    likedUserImageDataList: List<Any?>,
    likedUserNameList: List<String>,
    commentPreviewList: List<Pair<String, String>>,
    time: String?,
    playingVideoUrl: String?,
    setPlayingVideoUrl: (String?) -> Unit,
    setLike: () -> Unit,
    setUnLike: () -> Unit,
    onLongClickImage: (url: String?) -> Unit,
    onLongClickVideo: (url: String?) -> Unit,
    onCommentClick: (postId: String?) -> Unit,
    onComplainPost: () -> Unit,
    onHidePost: () -> Unit,
    onDeletePost: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context: Context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                playWhenReady = true
            }
    }

    val pagerState = rememberPagerState(
        pageCount = {
            postData.size
        },
    )

    LaunchedEffect(pagerState.currentPage) {
        if (exoPlayer.isPlaying) {
            setPlayingVideoUrl.invoke(null)
        }
    }

    Column(
        modifier = modifier,
    ) {
        PostViewTop(
            modifier = Modifier,
            isOwnPost = isOwnPost,
            userImageData = userImageData,
            userName = userName,
            onComplainPost = onComplainPost,
            onHidePost = onHidePost,
            onDeletePost = onDeletePost,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(postRatio ?: AppDefaults.ASPECT_RATIO_SQUARE)
                .background(AppTheme.colors.generalColors.backgroundSoft200),
            contentAlignment = Alignment.BottomCenter,
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.generalColors.backgroundSoft200),
                state = pagerState,
            ) { index ->
                postData.getOrNull(index)?.let {
                    key(it.first) {
                        val post = postData.getOrNull(index)
                        val url = post?.first as? String
                        val isImage = post?.second == CustomMediaType.IMAGE.value
                        val isVideo = post?.second == CustomMediaType.VIDEO.value

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(postRatio ?: AppDefaults.ASPECT_RATIO_SQUARE),
                        ) {
                            AppAsyncImageLoader.Load(
                                data = it.first,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(postRatio ?: AppDefaults.ASPECT_RATIO_SQUARE)
                                    .background(AppTheme.colors.generalColors.backgroundSoft200)
                                    .combinedClickable(
                                        onClick = {
                                            if (isImage) {
                                                onLongClickImage(url)
                                            } else if (isVideo) {
                                                onLongClickVideo(url)
                                            }
                                        },
                                        onLongClick = {
                                            if (isImage) {
                                                onLongClickImage(url)
                                            } else if (isVideo) {
                                                onLongClickVideo(url)
                                            }
                                        },
                                    ),
                                contentScale = ContentScale.Fit,
                            )

                            if (isVideo && playingVideoUrl != url) {
                                PostViewPlayButtonComponent(
                                    modifier = Modifier
                                        .align(Alignment.Center),
                                    onClick = {
                                        url?.toUri()?.let { uri -> MediaItem.fromUri(uri) }?.let { mediaItem ->
                                            exoPlayer.setMediaItem(mediaItem)
                                            exoPlayer.prepare()
                                            setPlayingVideoUrl.invoke(url)
                                        }
                                    },
                                )
                            }

                            if (playingVideoUrl == url && isVideo) {
                                AndroidView(
                                    factory = {
                                        PlayerView(context)
                                            .apply {
                                                useController = false
                                                player = exoPlayer
                                                keepScreenOn = true
                                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                                            }
                                    },
                                    modifier = Modifier
                                        .fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }

            if (postData.size > AppDefaults.ONE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.spacing.spacingSmallest),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(postData.size) { index ->
                        key(index) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = AppTheme.spacing.spacingTiny)
                                    .size(AppTheme.dimens.dp8)
                                    .background(
                                        color = if (index == pagerState.currentPage) AppTheme.colors.generalColors.foregroundWhite else AppTheme.colors.generalColors.foregroundSecondary,
                                        shape = AppTheme.shapes.radiusCircle,
                                    ),
                            )
                        }
                    }
                }
            }
        }

        PostViewActionButton(
            postId = postId,
            isLiked = isLiked,
            likedCount = likedCount,
            commentCount = commentCount,
            setLike = setLike,
            setUnLike = setUnLike,
            onCommentClick = onCommentClick,
        )

        PostViewLikedPreview(
            likedUserImageDataList = likedUserImageDataList,
            likedUserNameList = likedUserNameList,
            likeCount = likedCount,
        )

        PostViewCommentPreview(
            commentPreviewList = commentPreviewList,
        )

        time?.let {
            Text(
                text = time,
                style = AppTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(horizontal = AppTheme.spacing.spacingMedium)
                    .padding(bottom = AppTheme.spacing.spacingMedium),
            )
        }
    }
}

@Composable
private fun PostViewTop(
    userImageData: Any?,
    isOwnPost: Boolean,
    userName: String?,
    onComplainPost: () -> Unit,
    onHidePost: () -> Unit,
    onDeletePost: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.spacing.spacingSmall),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserImageView(
            data = userImageData,
            size = AppTheme.dimens.dp40,
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium),
        )

        userName?.let {
            Text(
                text = userName,
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colors.generalColors.textPrimary,
                modifier = Modifier
                    .weight(AppDefaults.WEIGHT_FULL)
                    .padding(start = AppTheme.spacing.spacingSmall),
            )
        }

        IconButton(
            onClick = {
                expanded = true
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(AppDefaults.DEGREE_90),
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    if (!isOwnPost) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Şikayet Et", // TODO: Localize
                                )
                            },
                            onClick = {
                                expanded = false
                                onComplainPost.invoke()
                            },
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Gizle", // TODO: Localize
                                )
                            },
                            onClick = {
                                expanded = false
                                onHidePost.invoke()
                            },
                        )
                    }

                    if (isOwnPost) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Kaldır", // TODO: Localize
                                )
                            },
                            onClick = {
                                expanded = false
                                onDeletePost.invoke()
                            },
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun PostViewActionButton(
    postId: String?,
    isLiked: Boolean,
    likedCount: Int?,
    commentCount: Int?,
    setLike: () -> Unit,
    setUnLike: () -> Unit,
    onCommentClick: (postId: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = AppTheme.spacing.spacingMedium)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp24)
                .noRippleClickable {
                    if (isLiked) {
                        setUnLike()
                    } else {
                        setLike()
                    }
                },
            content = {
                Crossfade(
                    targetState = isLiked,
                ) { isLikedAnimated ->
                    if (isLikedAnimated) {
                        Image(
                            painter = painterResource(resourcesR.drawable.img_heart_filled),
                            contentDescription = "like",
                            modifier = Modifier
                                .size(AppTheme.dimens.dp24),
                        )
                    } else {
                        Image(
                            painter = painterResource(resourcesR.drawable.img_heart),
                            contentDescription = "like",
                            modifier = Modifier
                                .size(AppTheme.dimens.dp24),
                        )
                    }
                }
            },
        )

        if (likedCount != null) {
            if (likedCount > AppDefaults.ZERO) {
                Text(
                    text = likedCount.toString(),
                    style = AppTheme.typography.helperText.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmallest),
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp24)
                .noRippleClickable {
                    onCommentClick.invoke(postId)
                },
            content = {
                Image(
                    painter = painterResource(resourcesR.drawable.img_comment_version_three),
                    contentDescription = "comment",
                    modifier = Modifier
                        .size(AppTheme.dimens.dp24),
                )
            },
        )

        if (commentCount != null) {
            if (commentCount > AppDefaults.ZERO) {
                Text(
                    text = commentCount.toString(),
                    style = AppTheme.typography.helperText.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = AppTheme.colors.generalColors.textPrimary,
                    modifier = Modifier
                        .padding(start = AppTheme.spacing.spacingSmallest),
                )
            }
        }

        /*
        Box(
            modifier = Modifier
                .padding(start = AppTheme.spacing.spacingMedium)
                .size(AppTheme.dimens.dp24)
                .noRippleClickable {
                },
            content = {
                Image(
                    painter = painterResource(resourcesR.drawable.img_send_version_two),
                    contentDescription = "send",
                    modifier = Modifier
                        .size(AppTheme.dimens.dp24),
                )
            },
        )
         */
    }
}

@Composable
private fun PostViewLikedPreview(
    likedUserImageDataList: List<Any?>,
    likedUserNameList: List<String>,
    likeCount: Int?,
    modifier: Modifier = Modifier,
) {
    if (likedUserImageDataList.size >= PostView.LIKED_USER_IMAGE_DATA_LIST_MIN_SIZE && likedUserNameList.size >= PostView.LIKED_USER_IMAGE_DATA_LIST_MIN_SIZE) {
        Row(
            modifier = modifier
                .padding(top = AppTheme.spacing.spacingMedium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = AppTheme.spacing.spacingMedium),
                contentAlignment = Alignment.CenterStart,
            ) {
                likedUserImageDataList.take(PostView.LIKED_USER_IMAGE_DATA_LIST_MAX_SIZE).fastForEachIndexed { index, item ->
                    key(item) {
                        Box(
                            modifier = Modifier
                                .padding(start = (index * PostView.LIKED_USER_IMAGE_PADDING_VALUE).dp)
                                .size(if (index == AppDefaults.ZERO) AppTheme.dimens.dp18 else AppTheme.dimens.dp24)
                                .background(
                                    color = AppTheme.colors.generalColors.foregroundWhite,
                                    shape = AppTheme.shapes.radiusCircle,
                                )
                                .clip(AppTheme.shapes.radiusCircle),
                            contentAlignment = Alignment.Center,
                        ) {
                            AppAsyncImageLoader.Load(
                                data = item,
                                modifier = Modifier
                                    .size(AppTheme.dimens.dp18)
                                    .background(
                                        color = AppTheme.colors.generalColors.backgroundSoft200,
                                        shape = AppTheme.shapes.radiusCircle,
                                    )
                                    .clip(AppTheme.shapes.radiusCircle),
                            )
                        }
                    }
                }
            }

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.generalColors.textTertiary,
                        fontSize = AppTheme.dimens.sp14,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    block = {
                        append(likedUserNameList.random())
                    },
                )

                append(" ")

                withStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.generalColors.textTertiary,
                        fontSize = AppTheme.dimens.sp14,
                        fontWeight = FontWeight.Normal,
                    ),
                    block = {
                        append("ve diğer kişiler beğendi") // TODO: Localize
                    },
                )
            }

            Text(
                text = annotatedString,
                maxLines = AppDefaults.LINE_LIMIT_SINGLE,
                lineHeight = TextUnit.Unspecified,
                modifier = Modifier
                    .padding(start = AppTheme.spacing.spacingSmall)
                    .padding(end = AppTheme.spacing.spacingMedium),
            )
        }
    } else if (likeCount != null) {
        if (likeCount <= AppDefaults.ZERO) {
            Text(
                text = "Henüz kimse beğenmedi. İlk beğenen sen ol", // TODO: Localize
                maxLines = AppDefaults.LINE_LIMIT_SINGLE,
                style = AppTheme.typography.bodyMediumCompact,
                color = AppTheme.colors.generalColors.textTertiary,
                modifier = Modifier
                    .padding(top = AppTheme.spacing.spacingMedium)
                    .padding(bottom = AppTheme.spacing.spacingSmall)
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
            )
        }
    }
}

@Composable
private fun PostViewCommentPreview(
    commentPreviewList: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    commentPreviewList.fastForEach { item ->
        key(item.second) {
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.generalColors.textPrimary,
                        fontSize = AppTheme.dimens.sp14,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    block = {
                        append(item.first)
                    },
                )

                append(" ")

                withStyle(
                    style = SpanStyle(
                        color = AppTheme.colors.generalColors.textTertiary,
                        fontSize = AppTheme.dimens.sp14,
                        fontWeight = FontWeight.Normal,
                    ),
                    block = {
                        append(item.second)
                    },
                )
            }

            Text(
                text = annotatedString,
                lineHeight = TextUnit.Unspecified,
                modifier = modifier
                    .padding(horizontal = AppTheme.spacing.spacingMedium),
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            LazyColumn {
                itemsIndexed(
                    items = FakeMockDashboard.itemList,
                    key = { index, item ->
                        "${item.uuid}-$index"
                    },
                    itemContent = { index, item ->
                        PostView.Primary(
                            postId = UUID.randomUUID().toString(),
                            userImageData = item.userImageData,
                            userName = item.userName,
                            postData = listOf(Pair(item.postData, index)),
                            postRatio = item.postRatio,
                            isLiked = item.isLiked,
                            likedCount = item.likedCount,
                            commentCount = item.commentCount,
                            likedUserImageDataList = item.likedUserImageDataList,
                            likedUserNameList = item.likedUserNameList,
                            commentPreviewList = item.commentPreviewList,
                            time = item.time,
                            playingVideoUrl = null,
                            setPlayingVideoUrl = {},
                            setLike = {},
                            setUnLike = {},
                            onLongClickImage = {},
                            onLongClickVideo = {},
                            onCommentClick = {},
                            onComplainPost = {},
                            onHidePost = {},
                            onDeletePost = {},
                            modifier = Modifier,
                            isOwnPost = false,
                        )
                    },
                )
            }
        }
    }
}
