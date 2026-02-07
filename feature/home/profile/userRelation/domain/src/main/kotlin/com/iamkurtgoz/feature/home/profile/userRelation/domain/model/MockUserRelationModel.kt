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
package com.iamkurtgoz.feature.home.profile.userRelation.domain.model

data object MockUserRelationModel {
    val list: List<UserRelationUIItemModel> = listOf(
        UserRelationUIItemModel(
            id = "1",
            name = "Hakan Yılmaz",
            username = "hakanyilmaz",
            summary = "Baş Antrenör",
            imageUrl = "https://randomuser.me/api/portraits/men/45.jpg",
            isFollow = true,
            isFollowRequest = false,
            isCurrentUser = false,
        ),
        UserRelationUIItemModel(
            id = "2",
            name = "Ayşe Demir",
            username = "aysedemir",
            summary = "Kondisyoner",
            imageUrl = "https://randomuser.me/api/portraits/women/68.jpg",
            isFollow = false,
            isFollowRequest = true,
            isCurrentUser = false,
        ),
        UserRelationUIItemModel(
            id = "3",
            name = "Mehmet Çelik",
            username = "mehmetcelik",
            summary = "Asistan Antrenör",
            imageUrl = "https://randomuser.me/api/portraits/men/34.jpg",
            isFollow = true,
            isFollowRequest = false,
            isCurrentUser = false,
        ),
        UserRelationUIItemModel(
            id = "4",
            name = "Zeynep Kaya",
            username = "zeynepkaya",
            summary = "Fizyoterapist",
            imageUrl = "https://randomuser.me/api/portraits/women/55.jpg",
            isFollow = false,
            isFollowRequest = false,
            isCurrentUser = true,
        ),
    )
}
