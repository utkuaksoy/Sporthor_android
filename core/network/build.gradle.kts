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
    alias(libs.plugins.build.logic.android.library)
}

android {
    namespace = "com.iamkurtgoz.core.network"
    hilt.enableAggregatingTask = true
}

dependencies {
    // Projects
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.common)

    // Network
    api(libs.network.retrofit)
    api(libs.network.retrofit.kotlinx.serialization.converter)
    api(libs.network.okhttp)
    api(libs.network.logging.interceptor)

    // Chucker
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.no.op)
}
