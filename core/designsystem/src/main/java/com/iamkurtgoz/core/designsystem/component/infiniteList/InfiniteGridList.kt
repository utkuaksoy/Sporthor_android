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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
fun <T : Listable> InfiniteGridList(
    itemList: ImmutableList<T>,
    columns: GridCells,
    modifier: Modifier = Modifier,
    listState: LazyGridState = rememberLazyGridState(),
    paginationLoading: Boolean = false,
    minLoadMoreItemsCount: Int = AppDefaults.LIST_PARAM_ITEMS_PER_PAGE,
    loadMore: (() -> Unit)? = null,
    refresh: (() -> Unit)? = null,
    isRefreshing: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(
        vertical = AppTheme.dimens.dp0,
        horizontal = AppTheme.dimens.dp0,
    ),
    pullRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AppTheme.dimens.dp2),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(AppTheme.dimens.dp2),
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
            columns = columns,
            listState = listState,
            paginationLoading = paginationLoading,
            minLoadMoreItemsCount = minLoadMoreItemsCount,
            loadMore = loadMore,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalArrangement = horizontalArrangement,
            header = header,
            footer = footer,
            rowContent = rowContent,
        )

        if (refresh != null) {
            PullToRefreshDefaults.Indicator(
                state = pullRefreshState,
                isRefreshing = isRefreshing,
                color = AppTheme.colors.generalColors.textPrimary,
                containerColor = AppTheme.colors.generalColors.foregroundPrimary,
            )
        }
    }
}

@Composable
private fun <T : Listable> ListContent(
    itemList: ImmutableList<T>,
    columns: GridCells,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    paginationLoading: Boolean = false,
    minLoadMoreItemsCount: Int = 0,
    loadMore: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(
        vertical = AppTheme.dimens.dp0,
        horizontal = AppTheme.dimens.dp0,
    ),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(AppTheme.dimens.dp2),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(AppTheme.dimens.dp2),
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    rowContent: @Composable (Int, T) -> Unit,
) {
    LazyVerticalGrid(
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        state = listState,
        columns = columns,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        if (header != null) {
            item(
                key = "static_item_key_header",
                span = { GridItemSpan(maxLineSpan) },
                content = {
                    header.invoke()
                },
            )
        }

        itemsIndexed(
            items = itemList,
            key = { _, item: T ->
                item.listableKey
            },
        ) { index, item ->
            rowContent(index, item)
        }

        if (paginationLoading) {
            item(
                key = "static_item_key_paginationLoading",
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
                key = "static_item_key_footer",
                span = { GridItemSpan(maxLineSpan) },
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
private fun LazyGridState.ShouldLoadMore(
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
