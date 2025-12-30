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
package com.iamkurtgoz.feature.auth.welcome.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeScaffold
import com.iamkurtgoz.core.resources.R

@Composable
internal fun WelcomeScreenBackgroundView(
    centerTopGuidelineFraction: Float = 0.15f,
    imgCenterSize: Dp = AppTheme.dimens.dp132,
    imgLeadingSize: Dp = AppTheme.dimens.dp120,
    imgTrailingSize: Dp = AppTheme.dimens.dp120,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val imgCenter = createRef()
        val (imgLeadingFirst, imgLeadingSecond) = createRefs()
        val (imgTrailingFirst, imgTrailingSecond) = createRefs()
        val imgCenterTopGuideline = createGuidelineFromTop(centerTopGuidelineFraction)

        Image(
            painter = painterResource(R.drawable.img_welcome_background),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Image(
            painter = painterResource(R.drawable.img_welcome_first),
            contentDescription = "img_welcome_first",
            modifier = Modifier
                .constrainAs(imgCenter) {
                    top.linkTo(imgCenterTopGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(imgCenterSize),
            contentScale = ContentScale.Fit,
        )

        Image(
            painter = painterResource(R.drawable.img_welcome_second),
            contentDescription = "img_welcome_second",
            modifier = Modifier
                .constrainAs(imgLeadingFirst) {
                    top.linkTo(imgCenter.top)
                    start.linkTo(parent.start, margin = -(imgLeadingSize / AppDefaults.TWO))
                }
                .padding(top = AppTheme.dimens.dp88)
                .size(imgLeadingSize),
            contentScale = ContentScale.Fit,
        )

        Image(
            painter = painterResource(R.drawable.img_welcome_fourth),
            contentDescription = "img_welcome_fourth",
            modifier = Modifier
                .constrainAs(imgLeadingSecond) {
                    top.linkTo(imgLeadingFirst.bottom)
                    start.linkTo(parent.start, margin = -(imgLeadingSize / AppDefaults.TWO))
                }
                .padding(top = AppTheme.dimens.dp128)
                .size(imgLeadingSize),
            contentScale = ContentScale.Fit,
        )

        Image(
            painter = painterResource(R.drawable.img_welcome_third),
            contentDescription = "img_welcome_third",
            modifier = Modifier
                .constrainAs(imgTrailingFirst) {
                    top.linkTo(imgCenter.top)
                    end.linkTo(parent.end, margin = -(imgTrailingSize / AppDefaults.TWO))
                }
                .padding(top = AppTheme.dimens.dp88)
                .size(imgTrailingSize),
            contentScale = ContentScale.Fit,
        )

        Image(
            painter = painterResource(R.drawable.img_welcome_fifth),
            contentDescription = "img_welcome_fifth",
            modifier = Modifier
                .constrainAs(imgTrailingSecond) {
                    top.linkTo(imgLeadingFirst.bottom)
                    end.linkTo(parent.end, margin = -(imgTrailingSize / AppDefaults.TWO))
                }
                .padding(top = AppTheme.dimens.dp128)
                .size(imgTrailingSize),
            contentScale = ContentScale.Fit,
        )
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeScaffold {
            WelcomeScreenBackgroundView()
        }
    }
}
