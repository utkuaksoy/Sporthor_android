plugins {
    alias(libs.plugins.build.logic.android.feature)
    alias(libs.plugins.build.logic.android.library.compose)
}

android {
    namespace = "com.iamkurtgoz.feature.home.profile.settings.accountSettings.data"
    hilt.enableAggregatingTask = true
}

dependencies {
    // Projects
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.resources)
    implementation(projects.core.common)

    // Projects
    implementation(projects.feature.home.profile.settings.accountSettings.domain)
}
