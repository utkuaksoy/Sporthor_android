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
package com.iamkurtgoz.feature.home.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastFirstOrNull
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenProfileRoute
import com.iamkurtgoz.feature.home.profile.component.ProfileAbout
import com.iamkurtgoz.feature.home.profile.component.ProfileActionButtons
import com.iamkurtgoz.feature.home.profile.component.ProfileDetailCurrentTeams
import com.iamkurtgoz.feature.home.profile.component.ProfileDetailFeaturedSkills
import com.iamkurtgoz.feature.home.profile.component.ProfileDetailProfileCard
import com.iamkurtgoz.feature.home.profile.component.ProfileInfo
import com.iamkurtgoz.feature.home.profile.component.ProfileNextMatches
import com.iamkurtgoz.feature.home.profile.component.ProfilePostImage
import com.iamkurtgoz.feature.home.profile.component.ProfileSegments
import com.iamkurtgoz.feature.home.profile.component.ProfileTeams
import com.iamkurtgoz.feature.home.profile.component.profileDetailCareerHistory
import com.iamkurtgoz.feature.home.profile.component.profileDetailTournaments
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileComponents
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileDetailComponents
import com.iamkurtgoz.feature.home.profile.domain.types.ProfileSegmentType
import com.iamkurtgoz.feature.home.profile.domain.types.toProfileComponent
import com.iamkurtgoz.feature.home.profile.domain.types.toProfileDetailComponents
import com.iamkurtgoz.feature.home.profile.domain.types.toProfileSegmentType
import com.iamkurtgoz.domain.model.enums.FetchParam
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Suppress("CyclomaticComplexMethod")
@Composable
internal fun ProfileScreenContent(
    state: ProfileScreenContract.State,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    setEvent: (ProfileScreenContract.Event) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.selectedSegmentState?.type, state.paginationHasNext) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalCount = layoutInfo.totalItemsCount
                lastVisibleIndex to totalCount
            }
            .filter { (lastVisibleIndex, totalCount) ->
                state.selectedSegmentState?.type.toProfileSegmentType() == ProfileSegmentType.Posts &&
                    lastVisibleIndex != null &&
                    totalCount > 0 &&
                    lastVisibleIndex >= totalCount - 1
            }
            .collectLatest {
                setEvent(ProfileScreenContract.Event.UserPosts(FetchParam.NEXT_PAGE))
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(
            bottom = contentPadding.calculateBottomPadding(),
        ),
    ) {
        itemsIndexed(
            items = state.profileModel?.components ?: listOf(),
            key = { index, item ->
                "$index${item.id}${item.type}${item.typeId}"
            },
            itemContent = { _, item ->
                when (item.id.toProfileComponent()) {
                    ProfileComponents.ProfileInfo -> ProfileInfo(
                        imageUrlData = item.data?.imageUrl,
                        name = state.profileModel?.info?.name,
                        postCount = item.data?.postCount,
                        followerCount = item.data?.followerCount,
                        followingCount = item.data?.followingCount,
                        setEvent = setEvent,
                        userId = item.data?.userId ?: "",
                    )

                    ProfileComponents.Teams -> ProfileTeams(
                        teams = item.data?.teams ?: listOf(),
                    )

                    ProfileComponents.ProfileAbout -> ProfileAbout(
                        title = item.data?.title ?: "",
                        description = item.data?.description ?: "",
                        extraInfo = item.data?.extraInfo ?: listOf(),
                    )

                    ProfileComponents.ProfileActionButtons -> ProfileActionButtons(
                        userId = item.data?.userId ?: "",
                        buttons = item.data?.buttons ?: listOf(),
                        setEvent = setEvent,
                    )

                    ProfileComponents.NextMatches -> ProfileNextMatches(
                        title = item.data?.title ?: "",
                        matches = item.data?.matches ?: listOf(),
                    )

                    ProfileComponents.Segments -> ProfileSegments(
                        selectedSegmentState = state.selectedSegmentState,
                        segments = item.data?.segments ?: listOf(),
                        setEvent = setEvent,
                    )

                    else -> {}
                }
            },
        )

        if (state.selectedSegmentState?.type.toProfileSegmentType() == ProfileSegmentType.Posts) {
            val posts = state.userPostsList?.posts ?: persistentListOf()
            val chunkedPosts = posts.chunked(AppDefaults.THREE)
            itemsIndexed(
                items = chunkedPosts,
                key = { index, row -> "post_row_$index-${row.firstOrNull()?.id}" },
            ) { rowIndex, rowItems ->
                Row(Modifier.fillMaxWidth()) {
                    rowItems.forEachIndexed { colIndex, item ->
                        Box(modifier = Modifier.weight(1f)) {
                            val index = rowIndex * AppDefaults.THREE + colIndex
                            ProfilePostImage(
                                imageUrlData = item.media?.map {
                                    Pair(it.url, it.type ?: 0)
                                } ?: listOf(),
                                onClick = {
                                    setEvent.invoke(ProfileScreenContract.Event.NavigateToPostDetail(index = index))
                                },
                                itemRatio = AppDefaults.ASPECT_RATIO_0_8,
                            )
                        }
                    }
                    if (rowItems.size < AppDefaults.THREE) {
                        repeat(AppDefaults.THREE - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        if (state.selectedSegmentState?.type.toProfileSegmentType() == ProfileSegmentType.PersonalInfo) {
            itemsIndexed(
                items = state.profileDetailModel?.components?.filterNotNull() ?: listOf(),
                key = { index, item ->
                    "$index${item.id}${item.type}${item.typeId}"
                },
                itemContent = { _, item ->
                    when (item.id.toProfileDetailComponents()) {
                        ProfileDetailComponents.ProfileCard -> ProfileDetailProfileCard(
                            imageUrl = item.data?.imageUrl,
                            name = item.data?.name,
                            nationalityName = item.data?.nationalityName,
                            flagIcon = item.data?.flagIcon,
                            birthDate = item.data?.birthDate,
                            height = item.data?.height,
                            weight = item.data?.weight,
                            modifier = Modifier
                                .padding(all = AppTheme.spacing.spacingMedium),
                        )

                        ProfileDetailComponents.CurrentTeams -> ProfileDetailCurrentTeams(
                            title = item.data?.title,
                            teams = item.data?.teams?.filterNotNull() ?: emptyList(),
                        )

                        ProfileDetailComponents.FeaturedSkills -> ProfileDetailFeaturedSkills(
                            state = state,
                            title = item.data?.title,
                            skills = item.data?.skills?.filterNotNull() ?: emptyList(),
                            setEvent = setEvent,
                        )

                        else -> {}
                    }
                },
            )

            state.profileDetailModel?.components?.fastFirstOrNull { it?.type == ProfileDetailComponents.Tournaments.type }?.data?.let {
                profileDetailTournaments(
                    title = it.title,
                    tournaments = it.tournaments?.filterNotNull() ?: emptyList(),
                )
            }

            state.profileDetailModel?.components?.fastFirstOrNull { it?.type == ProfileDetailComponents.CareerHistory.type }?.data?.let {
                profileDetailCareerHistory(
                    title = it.title,
                    items = it.items?.filterNotNull() ?: emptyList(),
                )
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            ProfileScreenContent(
                state = ProfileScreenContract.State(
                    isLoading = false,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenProfileRoute(
                        userId = null,
                    ),
                ),
                contentPadding = PaddingValues(),
                setEvent = { },
            )
        }
    }
}
