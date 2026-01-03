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
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.iamkurtgoz.app.enums.AppBuildType
import com.iamkurtgoz.app.enums.AppFlavor
import com.iamkurtgoz.app.enums.FlavorDimension
import java.util.Properties

plugins {
    alias(libs.plugins.build.logic.android.application)
    alias(libs.plugins.build.logic.android.application.compose)
    alias(libs.plugins.build.logic.sub.firebase)
    alias(libs.plugins.build.logic.sub.flavor)
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = computedVersionCode()
        versionName = libs.versions.versionName.get()
        setProperty("archivesBaseName", "$namespace-$versionName-$versionCode")
        ndk {
            debugSymbolLevel = "FULL"
        }

        testInstrumentationRunner = libs.versions.testRunner.get()
        vectorDrawables {
            useSupportLibrary = true
        }

        missingDimensionStrategy(FlavorDimension.contentType.name, AppFlavor.beta.name)
    }

    signingConfigs {
        create(AppFlavor.prod.name) {
            val keystoreFile = project.rootProject.file("app/sign/keystore.config")
            val properties = Properties()
            properties.load(keystoreFile.inputStream())

            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
            storeFile = file(properties.getProperty("storeFile"))
            storePassword = properties.getProperty("storePassword")
        }
    }

    applicationVariants.all {
        if (flavorName == AppFlavor.beta.name) {
            resValue("string", "app_name", "Sporthor - Beta - ${buildType.name}")
        } else {
            resValue("string", "app_name", "Sporthor")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AppBuildType.debug.applicationIdSuffix
            isMinifyEnabled = libs.versions.minifyEnabledDebug.get().toBoolean()
            isDebuggable = true
            versionNameSuffix = ".${gitCommitCount()}"
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            applicationIdSuffix = AppBuildType.release.applicationIdSuffix
            isMinifyEnabled = libs.versions.minifyEnabledRelease.get().toBoolean()
            isShrinkResources = true
            isDebuggable = false
            versionNameSuffix = ".${gitCommitCount()}"
            signingConfig = signingConfigs.findByName(AppFlavor.prod.name)
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
        }
    }

    buildFeatures.buildConfig = true
    hilt.enableAggregatingTask = true

    packaging {
        resources {
            excludes.add("/META-INF/LICENSE.md")
            excludes.add("/META-INF/LICENSE-notice.md")
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/versions/9/previous-compilation-data.bin")
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    // Test
    implementation(projects.konsistTest)

    // Projects
    implementation(projects.data)
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.designsystem)
    implementation(projects.core.commonUi)
    implementation(projects.core.common)
    implementation(projects.core.resources)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.resources)
    implementation(projects.core.firebase)
    implementation(projects.core.datastore)
    implementation(projects.core.connectivity)
    implementation(projects.core.network)
    implementation(projects.core.api)
    implementation(projects.core.timber)
    implementation(projects.core.signalrlib)

    // Projects - App
    implementation(projects.app.data)
    implementation(projects.app.domain)

    // Projects - Feature - Auth
    implementation(projects.feature.auth)
    implementation(projects.feature.auth.welcome)
    implementation(projects.feature.auth.welcome.data)
    implementation(projects.feature.auth.welcome.domain)
    implementation(projects.feature.auth.register)
    implementation(projects.feature.auth.register.data)
    implementation(projects.feature.auth.register.domain)
    implementation(projects.feature.auth.login)
    implementation(projects.feature.auth.login.data)
    implementation(projects.feature.auth.login.domain)
    implementation(projects.feature.auth.loginWithEmail)
    implementation(projects.feature.auth.loginWithEmail.data)
    implementation(projects.feature.auth.loginWithEmail.domain)
    implementation(projects.feature.auth.otp)
    implementation(projects.feature.auth.otp.data)
    implementation(projects.feature.auth.otp.domain)
    implementation(projects.feature.auth.userInfo)
    implementation(projects.feature.auth.userInfo.data)
    implementation(projects.feature.auth.userInfo.domain)
    implementation(projects.feature.auth.userName)
    implementation(projects.feature.auth.userName.data)
    implementation(projects.feature.auth.userName.domain)
    implementation(projects.feature.auth.forgetPassword)
    implementation(projects.feature.auth.forgetPassword.data)
    implementation(projects.feature.auth.forgetPassword.domain)

    // Projects - Feature - Home
    implementation(projects.feature.home)
    implementation(projects.feature.home.dashboard)
    implementation(projects.feature.home.dashboard.data)
    implementation(projects.feature.home.dashboard.domain)
    implementation(projects.feature.home.dashboard.webview)
    implementation(projects.feature.home.dashboard.webview.data)
    implementation(projects.feature.home.dashboard.webview.domain)
    implementation(projects.feature.home.search)
    implementation(projects.feature.home.search.data)
    implementation(projects.feature.home.search.domain)
    implementation(projects.feature.home.share)
    implementation(projects.feature.home.share.data)
    implementation(projects.feature.home.share.domain)
    implementation(projects.feature.home.share.complete)
    implementation(projects.feature.home.share.complete.data)
    implementation(projects.feature.home.share.complete.domain)
    implementation(projects.feature.home.chat)
    implementation(projects.feature.home.chat.data)
    implementation(projects.feature.home.chat.domain)
    implementation(projects.feature.home.chat.newChat)
    implementation(projects.feature.home.chat.newChat.data)
    implementation(projects.feature.home.chat.newChat.domain)
    implementation(projects.feature.home.chat.newChat.newGroupChat)
    implementation(projects.feature.home.chat.newChat.newGroupChat.data)
    implementation(projects.feature.home.chat.newChat.newGroupChat.domain)
    implementation(projects.feature.home.chat.chatMessaging)
    implementation(projects.feature.home.chat.chatMessaging.data)
    implementation(projects.feature.home.chat.chatMessaging.domain)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.data)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.domain)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.data)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.data)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup.domain)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.data)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile.domain)
    implementation(projects.feature.home.chat.chatMessaging.attachments)
    implementation(projects.feature.home.chat.chatMessaging.attachments.data)
    implementation(projects.feature.home.chat.chatMessaging.attachments.domain)
    implementation(projects.feature.home.profile)
    implementation(projects.feature.home.profile.data)
    implementation(projects.feature.home.profile.domain)
    implementation(projects.feature.home.profile.postDetail)
    implementation(projects.feature.home.profile.postDetail.data)
    implementation(projects.feature.home.profile.postDetail.domain)
    implementation(projects.feature.home.profile.userRelation)
    implementation(projects.feature.home.profile.userRelation.data)
    implementation(projects.feature.home.profile.userRelation.domain)
    implementation(projects.feature.home.profile.profileEdit)
    implementation(projects.feature.home.profile.profileEdit.data)
    implementation(projects.feature.home.profile.profileEdit.domain)
    implementation(projects.feature.home.profile.profileEdit.selectBranch)
    implementation(projects.feature.home.profile.profileEdit.selectBranch.domain)
    implementation(projects.feature.home.profile.profileEdit.selectBranch.data)
    implementation(projects.feature.home.profile.profileEdit.selectUserRole)
    implementation(projects.feature.home.profile.profileEdit.selectUserRole.data)
    implementation(projects.feature.home.profile.profileEdit.selectUserRole.domain)
    implementation(projects.feature.home.profile.settings)
    implementation(projects.feature.home.profile.settings.data)
    implementation(projects.feature.home.profile.settings.domain)
    implementation(projects.feature.home.profile.settings.aboutUs)
    implementation(projects.feature.home.profile.settings.aboutUs.data)
    implementation(projects.feature.home.profile.settings.aboutUs.domain)
    implementation(projects.feature.home.profile.settings.accountSettings)
    implementation(projects.feature.home.profile.settings.accountSettings.data)
    implementation(projects.feature.home.profile.settings.accountSettings.domain)
    implementation(projects.feature.home.customizeUserInfo)
    implementation(projects.feature.home.customizeUserInfo.data)
    implementation(projects.feature.home.customizeUserInfo.domain)
    implementation(projects.feature.home.onboarding)
    implementation(projects.feature.home.onboarding.data)
    implementation(projects.feature.home.onboarding.domain)
    implementation(projects.feature.home.mediaViewer)
    implementation(projects.feature.home.mediaViewer.data)
    implementation(projects.feature.home.mediaViewer.domain)
    implementation(projects.feature.home.storyViewer)
    implementation(projects.feature.home.storyViewer.data)
    implementation(projects.feature.home.storyViewer.domain)
    implementation(projects.feature.home.camerax)
    implementation(projects.feature.home.camerax.data)
    implementation(projects.feature.home.camerax.domain)
    implementation(projects.feature.home.selectTeam)
    implementation(projects.feature.home.selectTeam.data)
    implementation(projects.feature.home.selectTeam.domain)
    implementation(projects.feature.home.createTeam)
    implementation(projects.feature.home.createTeam.data)
    implementation(projects.feature.home.createTeam.domain)
    implementation(projects.feature.home.editTeam)
    implementation(projects.feature.home.editTeam.data)
    implementation(projects.feature.home.editTeam.domain)
    implementation(projects.feature.home.selectAddress)
    implementation(projects.feature.home.selectAddress.data)
    implementation(projects.feature.home.selectAddress.domain)
    implementation(projects.feature.home.sendClubAuthDocument)
    implementation(projects.feature.home.sendClubAuthDocument.data)
    implementation(projects.feature.home.sendClubAuthDocument.domain)
    implementation(projects.feature.home.successDocumentUploadScreen)
    implementation(projects.feature.home.successDocumentUploadScreen.data)
    implementation(projects.feature.home.successDocumentUploadScreen.domain)
    implementation(projects.feature.home.trainingScreen)
    implementation(projects.feature.home.trainingScreen.data)
    implementation(projects.feature.home.trainingScreen.domain)
    implementation(projects.feature.home.successAddTrainingGroup)
    implementation(projects.feature.home.successAddTrainingGroup.data)
    implementation(projects.feature.home.successAddTrainingGroup.domain)
    implementation(projects.feature.home.inviteGroupMembers)
    implementation(projects.feature.home.inviteGroupMembers.data)
    implementation(projects.feature.home.inviteGroupMembers.domain)
    implementation(projects.feature.home.inviteGroupMembers.addNewUser)
    implementation(projects.feature.home.inviteGroupMembers.addNewUser.data)
    implementation(projects.feature.home.inviteGroupMembers.addNewUser.domain)
    implementation(projects.feature.home.calendar)
    implementation(projects.feature.home.calendar.data)
    implementation(projects.feature.home.calendar.domain)
    implementation(projects.feature.home.calendarDetail)
    implementation(projects.feature.home.calendarDetail.data)
    implementation(projects.feature.home.calendarDetail.domain)
    implementation(projects.feature.home.addEvent)
    implementation(projects.feature.home.addEvent.data)
    implementation(projects.feature.home.addEvent.domain)
    implementation(projects.feature.home.selectEventDrafts)
    implementation(projects.feature.home.selectEventDrafts.data)
    implementation(projects.feature.home.selectEventDrafts.domain)
    implementation(projects.feature.home.notifications)
    implementation(projects.feature.home.notifications.data)
    implementation(projects.feature.home.notifications.domain)
    implementation(projects.feature.home.selectSportClub)
    implementation(projects.feature.home.selectSportClub.data)
    implementation(projects.feature.home.selectSportClub.domain)
    implementation(projects.feature.home.selectTrainingGroup)
    implementation(projects.feature.home.selectTrainingGroup.data)
    implementation(projects.feature.home.selectTrainingGroup.domain)
    implementation(projects.feature.home.editTrainingGroup)
    implementation(projects.feature.home.editTrainingGroup.data)
    implementation(projects.feature.home.editTrainingGroup.domain)
    implementation(projects.feature.home.editEvent)
    implementation(projects.feature.home.editEvent.data)
    implementation(projects.feature.home.editEvent.domain)
    implementation(projects.feature.home.coachList)
    implementation(projects.feature.home.coachList.data)
    implementation(projects.feature.home.coachList.domain)
    implementation(projects.feature.home.coachList.trainingGroups)
    implementation(projects.feature.home.coachList.trainingGroups.data)
    implementation(projects.feature.home.coachList.trainingGroups.domain)
    implementation(projects.feature.home.coachList.trainingGroups.updateCoach)
    implementation(projects.feature.home.coachList.trainingGroups.updateCoach.data)
    implementation(projects.feature.home.coachList.trainingGroups.updateCoach.domain)
    implementation(projects.feature.home.editTeam.selectBranch)
    implementation(projects.feature.home.editTeam.selectBranch.data)
    implementation(projects.feature.home.editTeam.selectBranch.domain)
    implementation(projects.feature.home.createTeam.selectBranch)
    implementation(projects.feature.home.createTeam.selectBranch.data)
    implementation(projects.feature.home.createTeam.selectBranch.domain)

    // AndroidX
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.splashscreen)

    // Google
    implementation(libs.google.accompanist.permission)

    // Compose
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
}

tasks.register("printReleaseCode") {
    println("${android.defaultConfig.versionName}-${android.defaultConfig.versionCode}")
}
