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
package com.iamkurtgoz.feature.home.chat.chatMessaging.attachments.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.component.infiniteList.InfiniteGridList
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MediaAttachmentsRow(
    modifier: Modifier = Modifier,
    imageList: ImmutableList<MediaAttachmentsRowItem>,
) {
    InfiniteGridList(
        itemList = imageList,
        columns = GridCells.Fixed(AppDefaults.GRID_CELL_COLUMN_THREE),
        rowContent = { index, item ->
            AppAsyncImageLoader.Load(
                data = item.data,
                contentDescription = "Gallery Image",
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(AppDefaults.ASPECT_RATIO_0_8)
                    .padding(all = 2.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                    },
                contentScale = ContentScale.Crop,
            )
        },
    )
}
