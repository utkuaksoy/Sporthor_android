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
plugins {
    alias(libs.plugins.build.logic.android.feature)
    alias(libs.plugins.build.logic.android.library.compose)
}

android {
    namespace = "com.iamkurtgoz.feature.auth"
    hilt.enableAggregatingTask = true
}

dependencies {
    // Projects
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.resources)

    // Projects - Auth
    implementation(projects.feature.auth.welcome)
    implementation(projects.feature.auth.register)
    implementation(projects.feature.auth.login)
    implementation(projects.feature.auth.loginWithEmail)
    implementation(projects.feature.auth.otp)
    implementation(projects.feature.auth.userInfo)
    implementation(projects.feature.auth.userName)
    implementation(projects.feature.auth.forgetPassword)
}
