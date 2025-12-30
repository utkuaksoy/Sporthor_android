package com.iamkurtgoz.app.enums

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
@Suppress("EnumEntryName", "EnumNaming")
enum class AppBuildType(val applicationIdSuffix: String? = null) {
    debug,
    release,
}
