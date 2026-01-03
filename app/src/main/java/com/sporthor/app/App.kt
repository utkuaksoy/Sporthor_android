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
package com.sporthor.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.sporthor.app.initializer.AppInitializer
import com.sporthor.`as`.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), SingletonImageLoader.Factory {

    @Inject
    lateinit var appInitializers: AppInitializer

    override fun onCreate() {
        super.onCreate()
        appInitializers.initialize()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        val memoryCacheInitializer: () -> MemoryCache = {
            MemoryCache.Builder()
                .maxSizePercent(
                    context = context,
                    percent = AppDefaults.IMAGE_LIB_MEMORY_CACHE_MAX_SIZE_PERCENT,
                )
                .build()
        }
        val diskCacheInitializer: () -> DiskCache = {
            DiskCache.Builder()
                .directory(this.cacheDir.resolve("image_cache"))
                .maxSizePercent(AppDefaults.IMAGE_LIB_DISK_CACHE_MAX_SIZE_PERCENT)
                .build()
        }

        val builder = ImageLoader.Builder(this)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache(memoryCacheInitializer.invoke())
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache(diskCacheInitializer)

        return if (BuildConfig.DEBUG) {
            builder.logger(DebugLogger()).build()
        } else {
            builder.build()
        }
    }
}
