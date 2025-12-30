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
package com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.MyFriendsDomainModel
import com.iamkurtgoz.domain.model.response.MyFriendsFriendItemDomainModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain.model.MyFriendsFriendItemUIModel
import com.iamkurtgoz.feature.home.chat.chatMessaging.chatMessagingDetailGroup.addUser.domain.model.MyFriendsUIModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MyFriendsUIMapper @Inject constructor(
    private val myFriendsFriendItemUIMapper: MyFriendsFriendItemUIMapper,
) : IMapper<MyFriendsDomainModel, MyFriendsUIModel> {
    override fun map(response: MyFriendsDomainModel): MyFriendsUIModel {
        return with(response) {
            MyFriendsUIModel(
                friends = friends?.filterNotNull()?.map(myFriendsFriendItemUIMapper::map),
            )
        }
    }
}

@Singleton
internal class MyFriendsFriendItemUIMapper @Inject constructor() : IMapper<MyFriendsFriendItemDomainModel, MyFriendsFriendItemUIModel> {
    override fun map(response: MyFriendsFriendItemDomainModel): MyFriendsFriendItemUIModel {
        return with(response) {
            MyFriendsFriendItemUIModel(
                id = id,
                imageUrl = imageUrl,
                isCurrentUser = isCurrentUser,
                isFollow = isFollow,
                name = name,
                summary = summary,
                username = username,
            )
        }
    }
}
