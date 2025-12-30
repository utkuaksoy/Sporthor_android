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
object HomeCoachListTrainingGroupsUpdateCoachScreenRoute : BaseScreenRoute<HomeCoachListTrainingGroupsUpdateCoachScreenRoute.Route> {

    @Keep
    @Serializable
    @Parcelize
    data class Route(
        val clubId: String?,
        val trainingGroupId: String?,
    ) : Parcelable

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
