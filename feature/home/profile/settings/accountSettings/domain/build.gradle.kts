plugins {
    alias(libs.plugins.build.logic.android.library)
}

android {
    namespace = "com.iamkurtgoz.feature.home.profile.settings.accountSettings.domain"
    hilt.enableAggregatingTask = true
}

dependencies {
    // Projects
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.common)
}
