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
package com.iamkurtgoz.feature.home.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.component.image.AppAsyncImageLoader
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold

@Composable
internal fun ProfilePostImage(
    imageUrlData: List<Pair<Any?, Int>>,
    itemRatio: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    AppAsyncImageLoader.Load(
        data = imageUrlData.firstOrNull()?.first,
        modifier = modifier
            .clickable {
                onClick.invoke()
            }
            .padding(all = AppTheme.spacing.spacingTiniest)
            .fillMaxWidth()
            .aspectRatio(itemRatio),
        contentScale = ContentScale.Crop,
    )
}

@Composable
@PreviewAppWithNightMode
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            ProfilePostImage(
                imageUrlData = listOf(Pair("https://images.unsplash.com/photo-1526232761682-d26e03ac148e?q=80&w=600&auto=format&fit=crop", 1)),
                itemRatio = AppDefaults.ASPECT_RATIO_0_8,
            )
        }
    }
}
