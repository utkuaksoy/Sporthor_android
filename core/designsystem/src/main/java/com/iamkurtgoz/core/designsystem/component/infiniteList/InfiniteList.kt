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
package com.iamkurtgoz.core.designsystem.component.infiniteList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.animation.AppLoadingView
import com.iamkurtgoz.core.designsystem.extension.ifTrue
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.domain.model.base.Listable
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Listable> InfiniteList(
    itemList: ImmutableList<T>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    paginationLoading: Boolean = false,
    minLoadMoreItemsCount: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
    pullRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    isRefreshing: Boolean? = null,
    loadMore: (() -> Unit)? = null,
    refresh: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(
        vertical = AppTheme.spacing.spacingNone,
        horizontal = AppTheme.spacing.spacingNone,
    ),
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    rowContent: @Composable (Int, T) -> Unit,
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .ifTrue(refresh != null) {
                this.pullToRefresh(
                    state = pullRefreshState,
                    isRefreshing = isRefreshing ?: false,
                    onRefresh = {
                        refresh?.invoke()
                    },
                )
            },
        contentAlignment = Alignment.TopCenter,
    ) {
        ListContent(
            itemList = itemList,
            listState = listState,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            paginationLoading = paginationLoading,
            minLoadMoreItemsCount = minLoadMoreItemsCount,
            loadMore = loadMore,
            header = header,
            footer = footer,
            rowContent = rowContent,
        )

        if (refresh != null) {
            PullToRefreshDefaults.Indicator(
                state = pullRefreshState,
                isRefreshing = isRefreshing ?: false,
                color = AppTheme.colors.generalColors.textPrimary,
                containerColor = AppTheme.colors.generalColors.backgroundSoft200,
            )
        }
    }
}

@Composable
private fun <T : Listable> ListContent(
    itemList: ImmutableList<T>,
    listState: LazyListState,
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    minLoadMoreItemsCount: Int,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    paginationLoading: Boolean = false,
    loadMore: (() -> Unit)? = null,
    fillMaxSize: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        vertical = AppTheme.spacing.spacingNone,
        horizontal = AppTheme.spacing.spacingNone,
    ),
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    rowContent: @Composable (Int, T) -> Unit,
) {
    LaunchedEffect(key1 = paginationLoading, key2 = itemList, key3 = listState) {
        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@LaunchedEffect
        if (paginationLoading && itemList.isNotEmpty() && lastVisibleItem.index == listState.layoutInfo.totalItemsCount - AppDefaults.TWO) {
            listState.scrollToItem(itemList.size)
        }
    }

    LazyColumn(
        modifier = modifier.ifTrue(fillMaxSize) {
            this.fillMaxSize()
        },
        state = listState,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        if (header != null) {
            item(
                key = "item_header",
                content = {
                    header.invoke()
                },
            )
        }

        itemsIndexed(
            items = itemList,
            key = { index, item: T ->
                "$index${item.listableKey}"
            },
        ) { index, item ->
            rowContent(index, item)
        }

        if (paginationLoading) {
            item(
                key = "item_paginationLoading",
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.spacing.spacingMedium)
                            .padding(bottom = AppTheme.appHomeSafeAreaPadding.calculateBottomPadding() + AppTheme.appHomeSafeAreaPadding.calculateBottomPadding()),
                        contentAlignment = Alignment.Center,
                        content = {
                            AppLoadingView()
                        },
                    )
                },
            )
        }

        if (footer != null) {
            item(
                key = "item_footer",
                content = {
                    footer.invoke()
                },
            )
        }
    }

    listState.ShouldLoadMore(
        onLoadMore = loadMore,
        minLoadMoreItemsCount = minLoadMoreItemsCount,
    )
}

@Composable
private fun LazyListState.ShouldLoadMore(
    minLoadMoreItemsCount: Int,
    onLoadMore: (() -> Unit)? = null,
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            if (layoutInfo.totalItemsCount == 0 || layoutInfo.totalItemsCount < minLoadMoreItemsCount) {
                false
            } else {
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true
                lastVisibleItem.index == layoutInfo.totalItemsCount - 1
            }
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) onLoadMore?.invoke()
            }
    }
}
