/*
 * Copyright 2024 Mehmet KURTGOZ
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
package com.iamkurtgoz.core.navigation.screenRoute

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.iamkurtgoz.core.navigation.base.BaseScreenRoute
import com.iamkurtgoz.core.navigation.model.base.CustomNavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Keep
@Serializable
object HomeCoachListScreenRoute : BaseScreenRoute<HomeCoachListScreenRoute.Route> {

    @Keep
    @Serializable
    @Parcelize
    data object Route : Parcelable

    override val typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>
        get() = mapOf(typeOf<Route>() to CustomNavType(Route::class.java, Route.serializer()))

    override fun composable(
        navGraphBuilder: NavGraphBuilder,
        deepLinks: List<NavDeepLink>,
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
    ) {
        navGraphBuilder.composable(
            route = Route::class,
            typeMap = typeMap,
            deepLinks = deepLinks,
            content = content,
        )
    }

    override fun toRoute(savedStateHandle: SavedStateHandle): Route {
        return savedStateHandle.toRoute<Route>(
            typeMap = typeMap,
        )
    }
}
