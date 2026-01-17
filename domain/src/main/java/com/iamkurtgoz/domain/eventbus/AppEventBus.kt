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
package com.iamkurtgoz.domain.eventbus

import com.iamkurtgoz.domain.eventbus.impl.AddEventTaskEventBus
import com.iamkurtgoz.domain.eventbus.impl.CalendarEventBus
import com.iamkurtgoz.domain.eventbus.impl.ChatDetailUserEventBus
import com.iamkurtgoz.domain.eventbus.impl.ChatEventBus
import com.iamkurtgoz.domain.eventbus.impl.ChatMessagingEventBus
import com.iamkurtgoz.domain.eventbus.impl.CreateClubEventBus
import com.iamkurtgoz.domain.eventbus.impl.CreateStoryComponent
import com.iamkurtgoz.domain.eventbus.impl.CreateTeamEventBus
import com.iamkurtgoz.domain.eventbus.impl.DashboardEventBus
import com.iamkurtgoz.domain.eventbus.impl.EditEventTaskEventBus
import com.iamkurtgoz.domain.eventbus.impl.EditTeamEventBus
import com.iamkurtgoz.domain.eventbus.impl.ProfileEditEventBus
import com.iamkurtgoz.domain.eventbus.impl.ProfileEventBus
import com.iamkurtgoz.domain.eventbus.impl.ProfileUserRelationEventBus
import com.iamkurtgoz.domain.eventbus.impl.ShareScreenEventBus
import com.iamkurtgoz.domain.model.enums.SignalRMessageType
import com.iamkurtgoz.domain.model.enums.UserActionFollowType
import com.iamkurtgoz.domain.model.enums.UserActionPostLikeType
import com.iamkurtgoz.domain.model.response.BranchesAttributeItemDomainModel
import com.iamkurtgoz.domain.model.response.LocalMediaDomainModel
import javax.inject.Singleton

@Singleton
object AppEventBus {

    val profileEventBus: ProfileEventBus = ProfileEventBus
    val dashboardEventBus: DashboardEventBus = DashboardEventBus
    val profileEditEventBus: ProfileEditEventBus = ProfileEditEventBus
    val profileUserRelationEventBus: ProfileUserRelationEventBus = ProfileUserRelationEventBus
    val chatEventBus: ChatEventBus = ChatEventBus
    val chatMessagingEventBus: ChatMessagingEventBus = ChatMessagingEventBus
    val shareScreenEventBus: ShareScreenEventBus = ShareScreenEventBus
    val chatDetailUserEventBus: ChatDetailUserEventBus = ChatDetailUserEventBus
    val createClubEventBus: CreateClubEventBus = CreateClubEventBus
    val addEventTaskEventBus: AddEventTaskEventBus = AddEventTaskEventBus
    val calendarEventBus: CalendarEventBus = CalendarEventBus
    val editEventTaskEventBus: EditEventTaskEventBus = EditEventTaskEventBus
    val createStoryComponent: CreateStoryComponent = CreateStoryComponent
    val editTeamEventBus: EditTeamEventBus = EditTeamEventBus
    val createTeamEventBus: CreateTeamEventBus = CreateTeamEventBus

    /******************************************************************************************
     ******************************************************************************************
     * Update Following Status
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun updateFollowingStatus(targetUserId: String, followType: UserActionFollowType) {
        profileEventBus.apply {
            val event = ProfileEventBus.Event.UpdateFollowingStatus(
                targetUserId = targetUserId,
                followType = followType,
            )
            send(event)
        }

        profileUserRelationEventBus.apply {
            val event = ProfileUserRelationEventBus.Event.UpdateFollowingStatus(
                targetUserId = targetUserId,
                followType = followType,
            )
            send(event)
        }

        chatDetailUserEventBus.apply {
            val event = ChatDetailUserEventBus.Event.UpdateFollowingStatus(
                targetUserId = targetUserId,
                followType = followType,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * Update Like Status
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun updateLikeStatus(postId: String, actionType: UserActionPostLikeType, likeCount: Int) {
        dashboardEventBus.apply {
            val event = DashboardEventBus.Event.UpdateLikeStatus(
                postId = postId,
                actionType = actionType,
                likeCount = likeCount,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * Receive New Message
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun receiveNewMessage(messageUserId: String?, displayName: String?, messageContent: String?, type: SignalRMessageType?, attachment: String?) {
        chatEventBus.apply {
            val event = ChatEventBus.Event.ReceiveNewMessage(
                messageUserId = messageUserId,
                displayName = displayName,
                messageContent = messageContent,
                type = type,
                attachment = attachment,
            )
            send(event)
        }
        chatMessagingEventBus.apply {
            val event = ChatMessagingEventBus.Event.ReceiveNewMessage(
                messageUserId = messageUserId,
                displayName = displayName,
                messageContent = messageContent,
                type = type,
                attachment = attachment,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * User Typing
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun receiveUserTyping(channelId: String?) {
        chatEventBus.apply {
            val event = ChatEventBus.Event.ReceiveUserTyping(
                channelId = channelId,
            )
            send(event)
        }
        chatMessagingEventBus.apply {
            val event = ChatMessagingEventBus.Event.ReceiveUserTyping(
                channelId = channelId,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * Media
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun mediaFileSaved(localMediaDomainModel: LocalMediaDomainModel) {
        shareScreenEventBus.apply {
            val event = ShareScreenEventBus.Event.MediaFileSaved(
                localMediaDomainModel = localMediaDomainModel,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * Update Selected Branch
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun updateSelectedBranch(branchImage: String?, branchTitle: String?, branchId: String?, branchAttribute: BranchesAttributeItemDomainModel) {
        profileEditEventBus.apply {
            val event = ProfileEditEventBus.Event.UpdateSelectedBranch(
                branchImage = branchImage,
                branchTitle = branchTitle,
                branchId = branchId,
                branchAttribute = branchAttribute,
            )
            send(event)
        }
        
        editTeamEventBus.apply {
            val event = EditTeamEventBus.Event.UpdateSelectedBranch(
                branchImage = branchImage,
                branchTitle = branchTitle,
                branchId = branchId,
                branchAttribute = branchAttribute,
            )
            send(event)
        }
        
        createTeamEventBus.apply {
            val event = CreateTeamEventBus.Event.UpdateSelectedBranch(
                branchImage = branchImage,
                branchTitle = branchTitle,
                branchId = branchId,
                branchAttribute = branchAttribute,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * Update Selected Address
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun updateSelectedAddress(title: String, address: String, city: String?, country: String?, latitude: Double?, longitude: Double?) {
        createClubEventBus.apply {
            val event = CreateClubEventBus.Event.SelectedAddressChanged(
                title = title,
                address = address,
                city = city,
                country = country,
                latitude = latitude,
                longitude = longitude,
            )
            send(event)
        }

        addEventTaskEventBus.apply {
            val event = AddEventTaskEventBus.Event.SelectedAddressChanged(
                title = title,
                address = address,
                city = city,
                country = country,
                latitude = latitude,
                longitude = longitude,
            )
            send(event)
        }

        editEventTaskEventBus.apply {
            val event = EditEventTaskEventBus.Event.SelectedAddressChanged(
                title = title,
                address = address,
                city = city,
                country = country,
                latitude = latitude,
                longitude = longitude,
            )
            send(event)
        }

        createStoryComponent.apply {
            val event = CreateStoryComponent.Event.SelectedAddressChanged(
                title = title,
                address = address,
                city = city,
                country = country,
                latitude = latitude,
                longitude = longitude,
            )
            send(event)
        }
    }

    /******************************************************************************************
     ******************************************************************************************
     * Fetch Story
     ******************************************************************************************
     ******************************************************************************************/

    suspend fun fetchStory() {
        dashboardEventBus.apply {
            val event = DashboardEventBus.Event.FetchStoryFeed
            send(event)
        }
    }
}
