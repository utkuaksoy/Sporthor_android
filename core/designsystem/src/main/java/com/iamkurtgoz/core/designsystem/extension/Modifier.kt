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
package com.iamkurtgoz.core.designsystem.extension

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.iamkurtgoz.core.common.contract.AppDefaults
import com.iamkurtgoz.core.designsystem.theme.AppTheme

private object ModifierDefaults {
    const val SHIMMER_ALPHA_START = 0.2f
    const val SHIMMER_ALPHA_MIDDLE = 1.0f
    const val SHIMMER_ALPHA_END = 0.2f
    const val DEFAULT_SHIMMER_WIDTH = 100f
    const val DEFAULT_SHIMMER_TARGET_VALUE: Float = 500f
    const val DEFAULT_SHIMMER_DURATION = 800
}

inline fun Modifier.ifTrue(
    value: Boolean,
    builder: Modifier.() -> Modifier,
): Modifier {
    val modifier = Modifier
    return then(if (value) modifier.builder() else modifier)
}

@Suppress("unused")
inline fun Modifier.ifFalse(
    value: Boolean,
    builder: Modifier.() -> Modifier,
): Modifier {
    val modifier = Modifier
    return then(if (!value) modifier.builder() else modifier)
}

fun Modifier.paddingFirstIndex(index: Int, start: Dp): Modifier {
    return if (index == AppDefaults.ZERO) {
        this.padding(start = start)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.fillMaxSizeStatus(
    active: Boolean,
    fraction: Float = 1f,
): Modifier {
    return if (active) {
        this.fillMaxSize(fraction)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.fillMaxWidthStatus(
    active: Boolean,
    fraction: Float = 1f,
): Modifier {
    return if (active) {
        this.fillMaxWidth(fraction)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.fillMaxHeightStatus(
    active: Boolean,
    fraction: Float = 1f,
): Modifier {
    return if (active) {
        this.fillMaxHeight(fraction)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.sizeNullable(size: Dp?): Modifier {
    return if (size != null) {
        this.size(size)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.verticalScrollNullable(scrollState: ScrollState?): Modifier {
    return if (scrollState != null) {
        this.verticalScroll(scrollState)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.defaultMinSizeNullable(size: Dp?): Modifier {
    return if (size != null) {
        this.defaultMinSize(minWidth = size, minHeight = size)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.defaultMinWidthNullable(minWidth: Dp?): Modifier {
    return if (minWidth != null) {
        this.defaultMinSize(minWidth = minWidth)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.defaultMinHeightNullable(minHeight: Dp?): Modifier {
    return if (minHeight != null) {
        this.defaultMinSize(minHeight = minHeight)
    } else {
        this
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.noRippleClickable(
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier {
    return this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.safeClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = Modifier.composed {
    this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = dropUnlessResumed {
            onClick.invoke()
        },
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.circle(): Modifier {
    return this.clip(CircleShape)
}

@Composable
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.shimmerLoadingAnimation(
    durationMillis: Int = ModifierDefaults.DEFAULT_SHIMMER_DURATION,
): Modifier {
    val shimmerColors = listOf(
        Color.DarkGray.copy(alpha = ModifierDefaults.SHIMMER_ALPHA_START),
        Color.DarkGray.copy(alpha = ModifierDefaults.SHIMMER_ALPHA_MIDDLE),
        Color.DarkGray.copy(alpha = ModifierDefaults.SHIMMER_ALPHA_END),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = ModifierDefaults.DEFAULT_SHIMMER_TARGET_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "",
    )

    return this.drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + ModifierDefaults.DEFAULT_SHIMMER_WIDTH, y = translateAnimation + ModifierDefaults.DEFAULT_SHIMMER_WIDTH),
            ),
        )
    }
}

@Composable
fun Modifier.imeAndStatusBarPadding(isKeyboardShow: Boolean): Modifier {
    return this.ifTrue(isKeyboardShow) {
        this.imePadding()
    }.ifFalse(isKeyboardShow) {
        this.padding(bottom = AppTheme.configuration.getSafeContentPaddingValues().calculateBottomPadding())
    }
}
