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
package com.iamkurtgoz.core.common.contract

import java.time.LocalDate

object AppDefaults {
    // General
    const val ZERO = 0
    const val ONE = 1
    const val TWO = 2
    const val THREE = 3
    const val FOUR = 4
    const val FIVE = 5
    const val SIX = 6
    const val SEVEN = 7
    const val EIGHT = 7
    const val TEN = 10
    const val DECIMAL_0_3 = 0.3

    const val GRID_CELL_COLUMN_TWO = 2
    const val GRID_CELL_COLUMN_THREE = 3

    const val LOCAL_MEDIA_LIST_SIZE = 10

    // Second
    const val DELAY_ONE_SECOND = 1000L

    // Image - Video Cache
    const val IMAGE_LIB_MEMORY_CACHE_MAX_SIZE_PERCENT = 0.25
    const val IMAGE_LIB_DISK_CACHE_MAX_SIZE_PERCENT = 0.02
    const val VIDEO_LIB_MEMORY_CACHE_MAX_SIZE_PERCENT = 0.25
    const val VIDEO_LIB_DISK_CACHE_MAX_SIZE_PERCENT = 0.02
    const val VIDEO_MAX_DURATION = 30

    // Color
    const val COMPOSE_COLORS_ZERO_TENTH_ALPHA = 0.0f
    const val COMPOSE_COLORS_ONE_TENTH_ALPHA = 0.1f
    const val COMPOSE_COLORS_ONE_TENTH_HALF_ALPHA = 0.15f
    const val COMPOSE_COLORS_TWO_TENTH_ALPHA = 0.2f
    const val COMPOSE_COLORS_QUARTER_ALPHA = 0.25f
    const val COMPOSE_COLORS_THREE_TENTH_ALPHA = 0.3f
    const val COMPOSE_COLORS_FOUR_TENTH_ALPHA = 0.4f
    const val COMPOSE_COLORS_HALF_ALPHA = 0.5f
    const val COMPOSE_COLORS_SIX_TENTH_ALPHA = 0.6f
    const val COMPOSE_COLORS_SEVEN_TENTH_ALPHA = 0.7f
    const val COMPOSE_COLORS_THREE_QUARTER_ALPHA = 0.75f
    const val COMPOSE_COLORS_EIGHT_TENTH_ALPHA = 0.8f
    const val COMPOSE_COLORS_ALMOST_FULL_ALPHA = 0.9f
    const val COMPOSE_COLORS_FULL_ALPHA = 1.0f

    // Font
    const val FONT_SCALE_DEFAULT = 1f

    // Others
    const val CHUCKER_MAX_CONTENT_LENGTH: Long = 950_000L
    const val APP_MIN_ANDROID_VERSION: Long = 2024090401

    // Network
    const val AUTHORIZATION_ERROR = 401
    const val TIMEOUT_MILLISECOND = 60L
    const val DELAY_REFRESH_TOKEN = 1L
    const val STATE_STOP_LISTENING_MILLISECOND = 5000L

    // Statics
    const val LINE_LIMIT_SINGLE = 1
    const val LINE_LIMIT_DOUBLE = 2
    const val LIST_PARAM_PAGE = 1
    const val LIST_PARAM_ITEMS_PER_PAGE = 12
    const val FORTUNE_TELLER_MAXIMUM_RATING = 5
    const val ASPECT_RATIO_0_5 = 0.5f
    const val ASPECT_RATIO_0_56 = 0.56f
    const val ASPECT_RATIO_0_75 = 0.75f
    const val ASPECT_RATIO_0_8 = 0.8f
    const val ASPECT_RATIO_SQUARE = 1f
    const val ASPECT_RATIO_1_15 = 1.15f
    const val ASPECT_RATIO_1_25 = 1.25f
    const val ASPECT_RATIO_1_5 = 1.5f
    const val ASPECT_RATIO_1_75 = 1.75f
    const val ASPECT_RATIO_2 = 2f
    const val WEIGHT_FULL = 1f
    const val WEIGHT_NONE = 0f
    const val WEIGHT_0_3 = 0.3f
    const val WEIGHT_0_35 = 0.35f
    const val WEIGHT_0_65 = 0.65f
    const val WEIGHT_0_7 = 0.7f
    const val FLOAT_0 = 0f

    // SCALE
    const val SCALE_0_6 = 0.6f
    const val SCALE_1 = 1f
    const val SCALE_1_2 = 1.2f
    const val SCALE_1_5 = 1.5f
    const val SCALE_2 = 2f

    // DEGREES
    const val DEGREE_90 = 90f

    // Day
    const val DAY_MIN = 1
    const val DAY_MAX = 31
    const val DAY_MIN_LENGTH = 1
    const val DAY_MAX_LENGTH = 2
    const val MONTH_MIN = 1
    const val MONTH_MAX = 12
    const val MONTH_MIN_LENGTH = 1
    const val MONTH_MAX_LENGTH = 2
    const val YEAR_MIN = 1938
    val YEAR_MAX = LocalDate.now().year
    const val YEAR_MIN_LENGTH = 1
    const val YEAR_MAX_LENGTH = 4
}
