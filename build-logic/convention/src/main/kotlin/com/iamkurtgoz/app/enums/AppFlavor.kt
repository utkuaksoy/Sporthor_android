package com.iamkurtgoz.app.enums

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName", "EnumNaming")
enum class AppFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    beta(FlavorDimension.contentType, "beta"),
    prod(FlavorDimension.contentType),
}
