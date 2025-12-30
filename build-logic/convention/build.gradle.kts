/*
 * Copyright 2024 HappyGuestTravelAndroid
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
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = libs.plugins.build.logic.group.get().pluginId

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
    compileOnly(libs.spotless.gradle.plugin)
    compileOnly(libs.ktlint.gradle.plugin)
    compileOnly(libs.secrets.gradle.plugin)
}

gradlePlugin {
    plugins {
        // BASE
        register("androidApplication") {
            id = libs.plugins.build.logic.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.build.logic.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.build.logic.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.build.logic.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibraryDesignSystemCompose") {
            id = libs.plugins.build.logic.android.library.design.system.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeDesignSystemConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.build.logic.android.feature.get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        // SUB
        register("androidTimber") {
            id = libs.plugins.build.logic.sub.timber.get().pluginId
            implementationClass = "SubAndroidTimberConventionPlugin"
        }
        register("androidHilt") {
            id = libs.plugins.build.logic.sub.hilt.get().pluginId
            implementationClass = "SubAndroidHiltConventionPlugin"
        }
        register("androidKtlint") {
            id = libs.plugins.build.logic.sub.ktlint.get().pluginId
            implementationClass = "SubAndroidKtlintConventionPlugin"
        }
        register("androidSpotless") {
            id = libs.plugins.build.logic.sub.spotless.get().pluginId
            implementationClass = "SubAndroidSpotlessConventionPlugin"
        }
        register("androidFirebase") {
            id = libs.plugins.build.logic.sub.firebase.get().pluginId
            implementationClass = "SubAndroidFirebaseConventionPlugin"
        }
        register("androidDetekt") {
            id = libs.plugins.build.logic.sub.detekt.get().pluginId
            implementationClass = "SubAndroidDetektConventionPlugin"
        }
        register("androidCoil") {
            id = libs.plugins.build.logic.sub.coil.get().pluginId
            implementationClass = "SubAndroidCoilConventionPlugin"
        }
        register("androidTest") {
            id = libs.plugins.build.logic.sub.test.get().pluginId
            implementationClass = "SubAndroidTestConventionPlugin"
        }
        register("androidFlavor") {
            id = libs.plugins.build.logic.sub.flavor.get().pluginId
            implementationClass = "SubAndroidFlavorConventionPlugin"
        }
    }
}
