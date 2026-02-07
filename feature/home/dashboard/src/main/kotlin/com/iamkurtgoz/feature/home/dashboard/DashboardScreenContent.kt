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
package com.iamkurtgoz.feature.home.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.post.PostView
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingView
import com.iamkurtgoz.core.designsystem.component.button.AppButton
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteList
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.model.home.mediaViewer.HomeScreenMediaViewerScreenNavigateModel
import com.iamkurtgoz.core.navigation.model.home.share.HomeScreenShareRouteScreenNavigateModel
import com.iamkurtgoz.domain.model.enums.FetchParam
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.feature.home.dashboard.component.StoriesRow
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardScreenContent(
    state: DashboardScreenContract.State,
    contentPadding: PaddingValues,
    setEvent: (DashboardScreenContract.Event) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val isInitialLoading = state.isShimmerLoading && state.dashboardFeedList.isEmpty()

    if (isInitialLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            AppLoadingView()
        }
        return
    }

    InfiniteList(
        modifier = modifier,
        itemList = state.dashboardFeedList,
        contentPadding = contentPadding,
        listState = listState,
        loadMore = {
            setEvent.invoke(DashboardScreenContract.Event.DashboardFeed(FetchParam.NEXT_PAGE))
        },
        refresh = {
            setEvent.invoke(DashboardScreenContract.Event.DashboardFeed(FetchParam.RELOAD))
        },
        isRefreshing = state.paginationReloading,
        paginationLoading = state.paginationLoading,
        header = {
            StoriesRow(
                stories = state.storyFeedList?.stories ?: persistentListOf(),
                navigateToShareStory = {
                    val event = DashboardScreenContract.Event.NavigateToShare(
                        routeType = HomeScreenShareRouteScreenNavigateModel.CreateStory,
                    )
                    setEvent.invoke(event)
                },
                onStoryClick = { userId ->
                    val event = DashboardScreenContract.Event.ClickedStoryItem(
                        userId = userId,
                    )
                    setEvent.invoke(event)
                },
            )
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
                    val event = DashboardScreenContract.Event.SetPlayingVideoUrl(
                        playingVideoUrl = playingVideoUrl,
                    )
                    setEvent(event)
                },
                setLike = {
                    val event = DashboardScreenContract.Event.SetLikeStatus(
                        postId = item.id,
                        actionType = UserActionPostLikeType.Like,
                    )
                    setEvent(event)
                },
                setUnLike = {
                    val event = DashboardScreenContract.Event.SetLikeStatus(
                        postId = item.id,
                        actionType = UserActionPostLikeType.UnLike,
                    )
                    setEvent(event)
                },
                onLongClickImage = { imageData ->
                    val event = DashboardScreenContract.Event.NavigateToMediaViewer(
                        routeType = HomeScreenMediaViewerScreenNavigateModel.RemoteOrLocalImage(
                            imageData = imageData,
                        ),
                    )
                    setEvent(event)
                },
                onLongClickVideo = { url ->
                    val event = DashboardScreenContract.Event.NavigateToMediaViewer(
                        routeType = HomeScreenMediaViewerScreenNavigateModel.RemoteVideo(
                            videoUrl = url,
                        ),
                    )
                    setEvent(event)
                },
                onCommentClick = { postId ->
                    val event = DashboardScreenContract.Event.SetCommentDialogShowPostId(
                        commentDialogShowPostId = postId,
                    )
                    setEvent(event)
                },
                onComplainPost = {
                    val event = DashboardScreenContract.Event.ShowReportDialog(postId = item.id)
                    setEvent(event)
                },
                onHidePost = {
                    val event = DashboardScreenContract.Event.SetHide(
                        postId = item.id,
                    )
                    setEvent(event)
                },
                onDeletePost = {
                    val event = DashboardScreenContract.Event.SetDelete(
                        postId = item.id,
                    )
                    setEvent(event)
                },
                modifier = Modifier
                    .padding(bottom = AppTheme.spacing.spacingLarge),
            )
        },
    )

    if (state.showReportDialogForPostId != null) {
        ReportBottomSheet(
            reportText = state.reportText,
            onDismiss = { setEvent(DashboardScreenContract.Event.DismissReportDialog) },
            onTextChanged = { setEvent(DashboardScreenContract.Event.UpdateReportText(it)) },
            onSendReport = {
                setEvent(
                    DashboardScreenContract.Event.ReportPost(
                        postId = state.showReportDialogForPostId,
                        reason = state.reportText,
                    ),
                )
                setEvent(DashboardScreenContract.Event.DismissReportDialog)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    reportText: String,
    onDismiss: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSendReport: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Şikayet Et",
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colors.generalColors.textPrimary,
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reportText,
                onValueChange = onTextChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Şikayet nedeninizi yazınız") },
                singleLine = false,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.generalColors.textPrimary,
                    unfocusedBorderColor = AppTheme.colors.generalColors.textPrimary,
                    disabledBorderColor = AppTheme.colors.generalColors.textPrimary.copy(alpha = 0.3f),
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton.PrimaryMedium(
                onClick = onSendReport,
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Gönder",
            )
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            DashboardScreenContent(
                state = DashboardScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                ),
                contentPadding = PaddingValues(),
                listState = rememberLazyListState(),
                setEvent = { },
            )
        }
    }
}
