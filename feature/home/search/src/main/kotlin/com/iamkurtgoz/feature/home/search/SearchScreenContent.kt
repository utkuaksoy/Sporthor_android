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
package com.iamkurtgoz.feature.home.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.extensions.getUserNameFirstChar
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.commonui.component.user.UserRow
import com.iamkurtgoz.core.commonui.component.user.UserRowFields
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.feature.home.search.domain.types.SocialSearchUIItemType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreenContent(
    state: SearchScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (SearchScreenContract.Event) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        if (state.isSearchTextFieldFocused && state.textSearch.value.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                    ) {
                        Text(
                            text = "Son aramalar", // TODO: Localize
                            modifier = Modifier
                                .weight(AppDefaults.WEIGHT_FULL)
                                .padding(top = AppTheme.spacing.spacingHuge)
                                .padding(bottom = AppTheme.spacing.spacingMedium),
                            style = AppTheme.typography.labelMedium,

                        )

                        Text(
                            text = "Tümünü Temizle", // TODO: Localize
                            modifier = Modifier
                                .padding(top = AppTheme.spacing.spacingHuge)
                                .padding(bottom = AppTheme.spacing.spacingMedium)
                                .clickable {
                                    val event = SearchScreenContract.Event.RemoveSearchHistory(
                                        id = null,
                                    )
                                    setEvent.invoke(event)
                                },
                            style = AppTheme.typography.labelMedium,
                            color = AppTheme.colors.generalColors.textDisabled,
                        )
                    }
                }

                itemsIndexed(
                    items = state.searchHistoryResultList?.histories?.toPersistentList() ?: persistentListOf(),
                    key = { index, item -> "${item.id}-$index" },
                ) { index, item ->
                    val isHeaderUser = remember(item.searchUser) { item.searchUser != null }
                    UserRow(
                        userHeaderData = item.searchUser?.image ?: item.searchUser?.name?.getUserNameFirstChar(),
                        isHeaderUser = isHeaderUser,
                        title = item.searchUser?.name ?: item.searchQuery,
                        subTitle = arrayOf(),
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingMedium)
                            .padding(top = AppTheme.spacing.spacingSmall),
                        onClickAction = {
                            if (item.searchUser != null) {
                                val event = SearchScreenContract.Event.ClickedSearchItem(
                                    userId = if (item.searchUser?.isTeam == false) item.searchUser?.userId else null,
                                    teamId = if (item.searchUser?.isTeam == true) item.searchUser?.userId else null,
                                )
                                setEvent.invoke(event)
                            }
                        },
                        trailingContent = {
                            UserRowFields.CloseIcon(
                                onClick = {
                                    val event = SearchScreenContract.Event.RemoveSearchHistory(
                                        id = item.id,
                                    )
                                    setEvent.invoke(event)
                                },
                            )
                        },
                    )
                }

                /*item {
                    Text(
                        text = "Tanıyor olabileceğin kişiler", // TODO: Localize
                        modifier = Modifier
                            .padding(vertical = AppTheme.spacing.spacingHuge)
                            .padding(horizontal = AppTheme.spacing.spacingMedium),
                        style = AppTheme.typography.labelMedium,
                    )
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        itemsIndexed(
                            items = state.suggestionUserList,
                            key = { index, item -> "${item.uuid}-$index" },
                        ) { index, item ->
                            SuggestionUserCard.Primary(
                                index = index,
                                modifier = Modifier
                                    .padding(
                                        start = if (index == AppDefaults.ZERO) AppTheme.spacing.spacingMedium else AppTheme.spacing.spacingSmall,
                                        end = AppTheme.spacing.spacingSmall,
                                    ),
                                isFollowing = item.isFollowing ?: false,
                                imageData = item.imageResId ?: 0,
                                badgeData = item.badgeResId,
                                containerText = item.name ?: "",
                                onButtonClick = { },
                                onClickAction = { },
                            )
                        }
                    }
                }*/
            }
        } else if (state.textSearch.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                item {
                    UserRow(
                        userHeaderData = null,
                        isHeaderUser = false,
                        title = state.textSearch.value,
                        subTitle = arrayOf(),
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingMedium)
                            .padding(top = AppTheme.spacing.spacingSmall),
                        onClickAction = {
                            val event = SearchScreenContract.Event.ClickedSearchItem(
                                searchTerm = state.textSearch.value,
                            )
                            setEvent.invoke(event)
                        },
                    )
                }

                itemsIndexed(
                    items = state.searchResultList?.searchList?.toPersistentList() ?: persistentListOf(),
                    key = { index, item -> "${item?.id}-$index" },
                ) { index, item ->
                    UserRow(
                        userHeaderData = item?.image,
                        isHeaderUser = true,
                        title = item?.name,
                        subTitle = arrayOf(),
                        modifier = Modifier
                            .padding(start = AppTheme.spacing.spacingMedium)
                            .padding(top = AppTheme.spacing.spacingSmall),
                        onClickAction = {
                            val event = SearchScreenContract.Event.ClickedSearchItem(
                                userId = if (item?.type == SocialSearchUIItemType.User) item.id else null,
                                teamId = if (item?.type == SocialSearchUIItemType.Team) item.id else null,
                            )
                            setEvent.invoke(event)
                        },
                    )
                }
            }
        } else {
            /*
            InfiniteGridList(
                itemList = state.imageList,
                columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_THREE),
                rowContent = { index, item ->
                    Image(
                        painter = painterResource(id = resourcesR.drawable.temp_img_coffee_cup),
                        contentDescription = "Gallery Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(AppDefaults.ASPECT_RATIO_0_8)
                            .padding(all = 2.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                setEvent.invoke(SearchScreenContract.Event.Initialize)
                            },
                        contentScale = ContentScale.Crop,
                    )
                },
            )
             */
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            SearchScreenContent(
                state = SearchScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    isSearchTextFieldFocused = true,
                ),
                setEvent = { },
            )
        }
    }
}
