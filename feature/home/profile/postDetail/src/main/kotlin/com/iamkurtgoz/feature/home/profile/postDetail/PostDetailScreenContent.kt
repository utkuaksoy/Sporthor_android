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
package com.iamkurtgoz.feature.home.profile.postDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.post.PostView
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenPostDetailRoute
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostDetailScreenContent(
    state: PostDetailScreenContract.State,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    setEvent: (PostDetailScreenContract.Event) -> Unit,
) {
    val listState: LazyListState = rememberLazyListState()

    LaunchedEffect(state.route.index, state.userPostFeedList.isNotEmpty(), !state.isLoading) { 
        state.route.index?.let { 
            listState.scrollToItem(index = it + 1)
        }
    }
    
    InfiniteList(
        modifier = modifier,
        itemList = state.userPostFeedList,
        contentPadding = contentPadding,
        listState = listState,
        loadMore = {
            setEvent.invoke(PostDetailScreenContract.Event.DashboardFeed(FetchParam.NEXT_PAGE))
        },
        refresh = {
            setEvent.invoke(PostDetailScreenContract.Event.DashboardFeed(FetchParam.RELOAD))
        },
        isRefreshing = state.paginationReloading,
        paginationLoading = state.paginationLoading,
        header = {
        },
        rowContent = { _, item ->
            PostView.Primary(
                postId = item.id,
                isOwnPost = item.userId == state.userId,
                userImageData = item.profileImageUrl,
                userName = item.username,
                postData = item.media?.map {
                    Pair(it.url, it.type ?: 0)
                } ?: listOf(),
                postRatio = AppDefaults.ASPECT_RATIO_0_75,
                isLiked = item.isLiked == true,
                likedCount = item.likeCount,
                commentCount = item.commentCount,
                likedUserImageDataList = item.lastLikedUsers?.mapNotNull { it.profileImageUrl } ?: listOf(),
                likedUserNameList = item.lastLikedUsers?.mapNotNull { it.username } ?: listOf(),
                commentPreviewList = item.commentPreviewList,
                time = item.time,
                playingVideoUrl = state.playingVideoUrl,
                setPlayingVideoUrl = { playingVideoUrl ->
                    val event = PostDetailScreenContract.Event.SetPlayingVideoUrl(
                        playingVideoUrl = playingVideoUrl,
                    )
                    setEvent(event)
                },
                setLike = {
                    val event = PostDetailScreenContract.Event.SetLikeStatus(
                        postId = item.id,
                        actionType = UserActionPostLikeType.Like,
                    )
                    setEvent(event)
                },
                setUnLike = {
                    val event = PostDetailScreenContract.Event.SetLikeStatus(
                        postId = item.id,
                        actionType = UserActionPostLikeType.UnLike,
                    )
                    setEvent(event)
                },
                onLongClickImage = { imageData ->
                    val event = PostDetailScreenContract.Event.NavigateToMediaViewer(
                        routeType = HomeScreenMediaViewerScreenNavigateModel.RemoteOrLocalImage(
                            imageData = imageData,
                        ),
                    )
                    setEvent(event)
                },
                onLongClickVideo = { url ->
                    val event = PostDetailScreenContract.Event.NavigateToMediaViewer(
                        routeType = HomeScreenMediaViewerScreenNavigateModel.RemoteVideo(
                            videoUrl = url,
                        ),
                    )
                    setEvent(event)
                },
                onCommentClick = { postId ->
                    val event = PostDetailScreenContract.Event.SetCommentDialogShowPostId(
                        commentDialogShowPostId = postId,
                    )
                    setEvent(event)
                },
                onComplainPost = {
                    val event = PostDetailScreenContract.Event.ShowReportDialog(postId = item.id)
                    setEvent(event)
                },
                onHidePost = {
                    val event = PostDetailScreenContract.Event.SetHide(
                        postId = item.id,
                    )
                    setEvent(event)
                },
                onDeletePost = {
                },
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.spacingLarge),
            )
        },
    )
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            PostDetailScreenContent(
                state = PostDetailScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    route = HomeScreenPostDetailRoute(
                        userId = null,
                        index = null,
                    ),
                ),
                setEvent = { },
                contentPadding = PaddingValues(),
            )
        }
    }
}
