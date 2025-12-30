plugins {
    alias(libs.plugins.build.logic.android.feature)
    alias(libs.plugins.build.logic.android.library.compose)
}

android {
    namespace = "com.iamkurtgoz.feature.home.editTeam.selectBranch.data"
    hilt.enableAggregatingTask = true
}

dependencies {
    // Projects
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.resources)
    implementation(projects.core.common)

    // Projects
    implementation(projects.feature.home.editTeam.selectBranch.domain)
}
