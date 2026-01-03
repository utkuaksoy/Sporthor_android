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
package com.iamkurtgoz.feature.home.dashboard.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.internal.PreviewAppWithNightMode
import com.iamkurtgoz.core.designsystem.theme.AppTheme
import com.iamkurtgoz.core.designsystem.theme.AppThemeSurface
import com.iamkurtgoz.core.navigation.HomeScreenDashboardWebviewRoute
import com.iamkurtgoz.core.navigation.model.home.webview.HomeScreenWebViewScreenNavigateModel
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebviewScreenContent(
    state: WebviewScreenContract.State,
    modifier: Modifier = Modifier,
    setEvent: (WebviewScreenContract.Event) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            setEvent(WebviewScreenContract.Event.SetLoadingStatus(true))
                            Timber.tag("WebViewClient").d(url)
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            setEvent(WebviewScreenContract.Event.SetLoadingStatus(false))
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            return false
                        }
                    }

                    val initialUrl = state.navigateRoute.routeType.url.toString()
                    loadUrl(initialUrl)
                    tag = initialUrl
                }
            },
            update = { webViewInstance ->
                val newUrl = state.navigateRoute.routeType.url.toString()
                if (webViewInstance.tag as? String != newUrl) {
                    setEvent(WebviewScreenContract.Event.SetLoadingStatus(true))
                    webViewInstance.loadUrl(newUrl)
                    webViewInstance.tag = newUrl
                }
            },
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@PreviewAppWithNightMode
@Composable
private fun Preview() {
    AppTheme {
        AppThemeSurface {
            WebviewScreenContent(
                state = WebviewScreenContract.State(
                    isLoading = true,
                    appBuildConfigStatePack = AppBuildConfigStatePack(),
                    appRemoteConfigStatePack = AppRemoteConfigStatePack(),
                    navigateRoute = HomeScreenDashboardWebviewRoute(
                        routeType = HomeScreenWebViewScreenNavigateModel(
                            url = "",
                            title = "",
                        ),
                    ),
                ),
                setEvent = { },
            )
        }
    }
}
