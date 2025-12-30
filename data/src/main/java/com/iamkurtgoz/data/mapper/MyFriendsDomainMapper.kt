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
package com.iamkurtgoz.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.data.model.MyFriendsFriendItemResponseModel
import com.iamkurtgoz.data.model.MyFriendsResponseModel
import com.iamkurtgoz.domain.model.response.MyFriendsDomainModel
import com.iamkurtgoz.domain.model.response.MyFriendsFriendItemDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MyFriendsDomainMapper @Inject constructor(
    private val myFriendsFriendItemDomainMapper: MyFriendsFriendItemDomainMapper,
) : IMapper<MyFriendsResponseModel, MyFriendsDomainModel> {
    override fun map(response: MyFriendsResponseModel): MyFriendsDomainModel {
        return with(response) {
            MyFriendsDomainModel(
                friends = friends?.filterNotNull()?.map(myFriendsFriendItemDomainMapper::map),
            )
        }
    }
}

@Singleton
internal class MyFriendsFriendItemDomainMapper @Inject constructor() : IMapper<MyFriendsFriendItemResponseModel, MyFriendsFriendItemDomainModel> {
    override fun map(response: MyFriendsFriendItemResponseModel): MyFriendsFriendItemDomainModel {
        return with(response) {
            MyFriendsFriendItemDomainModel(
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
