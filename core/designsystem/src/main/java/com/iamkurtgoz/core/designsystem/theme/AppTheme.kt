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
package com.iamkurtgoz.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.common.state.AppBuildConfigStatePack
import com.iamkurtgoz.core.common.state.AppRemoteConfigStatePack
import com.iamkurtgoz.core.designsystem.theme.composition.AppColors
import com.iamkurtgoz.core.designsystem.theme.composition.AppConfiguration
import com.iamkurtgoz.core.designsystem.theme.composition.AppDimens
import com.iamkurtgoz.core.designsystem.theme.composition.AppShapes
import com.iamkurtgoz.core.designsystem.theme.composition.AppSpacing
import com.iamkurtgoz.core.designsystem.theme.composition.AppTypography
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppBuildConfigStatePackConfiguration
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppColors
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppConfiguration
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppContentColor
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppDimens
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppEventBus
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppHomeSafeAreaPadding
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppPreferencesConfiguration
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppRemoteConfigStatePackConfiguration
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppShapes
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppSpacing
import com.iamkurtgoz.core.designsystem.theme.composition.LocalAppTypographies
import com.iamkurtgoz.core.designsystem.theme.composition.appConfiguration
import com.iamkurtgoz.core.designsystem.theme.composition.appDimens
import com.iamkurtgoz.core.designsystem.theme.composition.appShapes
import com.iamkurtgoz.core.designsystem.theme.composition.appSpacing
import com.iamkurtgoz.core.designsystem.theme.composition.asMaterialColors
import com.iamkurtgoz.core.designsystem.theme.composition.mediumShapes
import com.iamkurtgoz.core.designsystem.theme.configuration.typography.appDefaultTypography
import com.iamkurtgoz.core.designsystem.theme.configuration.typography.materialAppTypography
import com.iamkurtgoz.core.designsystem.util.AppThemeUtil
import com.iamkurtgoz.domain.dataStore.AppPreferences
import com.iamkurtgoz.domain.dataStore.FakeAppPreferences
import com.iamkurtgoz.domain.eventbus.AppEventBus

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appBuildConfigStatePack: AppBuildConfigStatePack = AppBuildConfigStatePack(),
    appRemoteConfigStatePack: AppRemoteConfigStatePack = AppRemoteConfigStatePack(),
    appPreferences: AppPreferences = FakeAppPreferences,
    appEventBus: AppEventBus = AppEventBus,
    content: @Composable () -> Unit,
) {
    val colorScheme by AppThemeUtil.getColorScheme()
    val view = LocalView.current
    SideEffect {
        AppThemeUtil.setAppearanceBarsColors(
            view = view,
            darkTheme = darkTheme,
            colorScheme = colorScheme,
            isAppearanceLightStatusBars = null,
            isAppearanceLightNavigationBars = null,
        )
    }

    ProvideAppResources(
        typography = appDefaultTypography,
        colors = colorScheme,
        shapes = appShapes,
        spacing = appSpacing,
        appDimens = appDimens,
        appConfiguration = appConfiguration,
        appBuildConfigStatePack = appBuildConfigStatePack,
        appRemoteConfigStatePack = appRemoteConfigStatePack,
        appPreferences = appPreferences,
        appEventBus = appEventBus,
    ) {
        MaterialTheme(
            colorScheme = colorScheme.asMaterialColors(),
            typography = materialAppTypography,
            shapes = mediumShapes,
            content = content,
        )
    }
}

@Composable
fun AppThemeSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = AppTheme.colors.generalColors.backgroundPrimary,
        contentColor = AppTheme.colors.generalColors.foregroundPrimary,
        content = content,
    )
}

@Composable
fun AppThemeScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = AppTheme.colors.generalColors.backgroundPrimary,
    contentColor: Color = AppTheme.colors.generalColors.foregroundPrimary,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}

@Composable
private fun ProvideAppResources(
    typography: AppTypography,
    colors: AppColors,
    shapes: AppShapes,
    spacing: AppSpacing,
    appDimens: AppDimens,
    appConfiguration: AppConfiguration,
    appBuildConfigStatePack: AppBuildConfigStatePack,
    appRemoteConfigStatePack: AppRemoteConfigStatePack,
    appPreferences: AppPreferences,
    appEventBus: AppEventBus,
    content: @Composable () -> Unit,
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    CompositionLocalProvider(
        LocalAppTypographies provides typography,
        LocalAppColors provides colorPalette,
        LocalAppShapes provides shapes,
        LocalAppSpacing provides spacing,
        LocalAppContentColor provides colorPalette.generalColors.foregroundPrimary,
        LocalAppDimens provides appDimens,
        LocalAppConfiguration provides appConfiguration,
        LocalDensity provides Density(LocalDensity.current.density, AppDefaults.FONT_SCALE_DEFAULT),
        LocalAppBuildConfigStatePackConfiguration provides appBuildConfigStatePack,
        LocalAppRemoteConfigStatePackConfiguration provides appRemoteConfigStatePack,
        LocalAppPreferencesConfiguration provides appPreferences,
        LocalAppHomeSafeAreaPadding provides PaddingValues(appDimens.dp0),
        LocalAppEventBus provides appEventBus,
    ) {
        ProvideTextStyle(
            value = typography.bodyMedium,
            content = content,
        )
    }
}

object AppTheme {
    val typography: AppTypography
        @Composable
        get() = LocalAppTypographies.current

    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current

    val spacing: AppSpacing
        @Composable
        get() = LocalAppSpacing.current

    val dimens: AppDimens
        @Composable
        get() = LocalAppDimens.current

    val configuration: AppConfiguration
        @Composable
        get() = LocalAppConfiguration.current

    val appBuildConfigStatePack: AppBuildConfigStatePack
        @Composable
        get() = LocalAppBuildConfigStatePackConfiguration.current

    val appRemoteConfigStatePack: AppRemoteConfigStatePack
        @Composable
        get() = LocalAppRemoteConfigStatePackConfiguration.current

    val appPreferences: AppPreferences
        @Composable
        get() = LocalAppPreferencesConfiguration.current

    val appHomeSafeAreaPadding: PaddingValues
        @Composable
        get() = LocalAppHomeSafeAreaPadding.current

    val appEventBus: AppEventBus
        @Composable
        get() = LocalAppEventBus.current
}
