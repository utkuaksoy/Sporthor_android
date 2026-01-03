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
    namespace = "com.iamkurtgoz.feature.home"
    hilt.enableAggregatingTask = true
}

dependencies {
    // Projects
    implementation(projects.domain)

    // Projects - Core
    implementation(projects.core.resources)

    // Projects - Home
    implementation(projects.feature.home.dashboard)
    implementation(projects.feature.home.dashboard.webview)
    implementation(projects.feature.home.search)
    implementation(projects.feature.home.share)
    implementation(projects.feature.home.share.complete)
    implementation(projects.feature.home.chat)
    implementation(projects.feature.home.chat.newChat)
    implementation(projects.feature.home.chat.chatMessaging)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailGroup.updateGroup)
    implementation(projects.feature.home.chat.chatMessaging.chatMessagingDetailUserProfile)
    implementation(projects.feature.home.chat.chatMessaging.attachments)
    implementation(projects.feature.home.chat.newChat.newGroupChat)
    implementation(projects.feature.home.profile)
    implementation(projects.feature.home.profile.postDetail)
    implementation(projects.feature.home.profile.userRelation)
    implementation(projects.feature.home.profile.profileEdit)
    implementation(projects.feature.home.profile.profileEdit.selectBranch)
    implementation(projects.feature.home.profile.profileEdit.selectUserRole)
    implementation(projects.feature.home.profile.settings)
    implementation(projects.feature.home.profile.settings.aboutUs)
    implementation(projects.feature.home.profile.settings.accountSettings)
    implementation(projects.feature.home.customizeUserInfo)
    implementation(projects.feature.home.onboarding)
    implementation(projects.feature.home.mediaViewer)
    implementation(projects.feature.home.storyViewer)
    implementation(projects.feature.home.camerax)
    implementation(projects.feature.home.selectTeam)
    implementation(projects.feature.home.createTeam)
    implementation(projects.feature.home.editTeam)
    implementation(projects.feature.home.selectAddress)
    implementation(projects.feature.home.sendClubAuthDocument)
    implementation(projects.feature.home.successDocumentUploadScreen)
    implementation(projects.feature.home.trainingScreen)
    implementation(projects.feature.home.successAddTrainingGroup)
    implementation(projects.feature.home.inviteGroupMembers)
    implementation(projects.feature.home.inviteGroupMembers.addNewUser)
    implementation(projects.feature.home.calendar)
    implementation(projects.feature.home.calendarDetail)
    implementation(projects.feature.home.addEvent)
    implementation(projects.feature.home.notifications)
    implementation(projects.feature.home.selectEventDrafts)
    implementation(projects.feature.home.selectSportClub)
    implementation(projects.feature.home.selectTrainingGroup)
    implementation(projects.feature.home.editTrainingGroup)
    implementation(projects.feature.home.editEvent)
    implementation(projects.feature.home.coachList)
    implementation(projects.feature.home.coachList.trainingGroups)
    implementation(projects.feature.home.coachList.trainingGroups.updateCoach)
    implementation(projects.feature.home.editTeam.selectBranch)
    implementation(projects.feature.home.createTeam.selectBranch)
}
