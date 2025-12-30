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
package com.iamkurtgoz.fake.model.response

import com.iamkurtgoz.domain.model.base.Listable
import java.util.UUID

// TODO: delete after connect dashboard api
data class MockDashboard(
    override val uuid: String?,
    val userImageData: Any?,
    val userName: String?,
    val postData: Any?,
    val postRatio: Float?,
    val isLiked: Boolean,
    val likedCount: Int,
    val commentCount: Int,
    val likedUserImageDataList: List<Any?>,
    val likedUserNameList: List<String>,
    val commentPreviewList: List<Pair<String, String>>,
    val time: String?,
) : Listable

object FakeMockDashboard {
    val itemList = listOf(
        MockDashboard(
            uuid = UUID.randomUUID().toString(),
            userImageData = "https://randomuser.me/api/portraits/men/23.jpg",
            userName = "Kenan Yıldız",
            postData = "https://images.pexels.com/photos/210186/pexels-photo-210186.jpeg",
            postRatio = (2200f / 1331f),
            isLiked = false,
            likedCount = 0,
            commentCount = 1,
            likedUserImageDataList = listOf(),
            likedUserNameList = listOf(),
            commentPreviewList = listOf(
                "Kenan Yıldız" to "Güzelmiş, neresi burası?",
            ),
            time = "1 gün önce",
        ),
        MockDashboard(
            uuid = UUID.randomUUID().toString(),
            userImageData = "https://randomuser.me/api/portraits/women/25.jpg",
            userName = "Dilara Çevik",
            postData = "https://images.pexels.com/photos/842711/pexels-photo-842711.jpeg",
            postRatio = (5472f / 3648f),
            isLiked = true,
            likedCount = 5,
            commentCount = 23,
            likedUserImageDataList = listOf(
                "https://randomuser.me/api/portraits/women/4.jpg",
                "https://randomuser.me/api/portraits/women/15.jpg",
                "https://randomuser.me/api/portraits/women/35.jpg",
                "https://randomuser.me/api/portraits/women/6.jpg",
                "https://randomuser.me/api/portraits/women/32.jpg",
            ),
            likedUserNameList = listOf(
                "Aslı Yıldız",
                "Charlie",
                "Aslı Yıldız",
                "Charlie",
                "Charlie",
            ),
            commentPreviewList = listOf(
                "Dilara Çevik" to "Bu gerçekten ilginç görünüyor.",
                "Aslı Yıldız" to "Bunu daha önce görmüştüm, çok güzel!",
            ),
            time = "1 gün önce",
        ),
        MockDashboard(
            uuid = UUID.randomUUID().toString(),
            userImageData = "https://randomuser.me/api/portraits/men/10.jpg",
            userName = "Emir Korkmaz",
            postData = "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg",
            postRatio = (5306f / 3770f),
            isLiked = false,
            likedCount = 12,
            commentCount = 4,
            likedUserImageDataList = listOf(
                "https://randomuser.me/api/portraits/men/3.jpg",
                "https://randomuser.me/api/portraits/women/9.jpg",
            ),
            likedUserNameList = listOf("Zeynep", "Arda"),
            commentPreviewList = listOf(
                "Emir Korkmaz" to "Harika bir kare!",
                "Zeynep" to "Manzara müthişmiş.",
            ),
            time = "1 gün önce",
        ),
        MockDashboard(
            uuid = UUID.randomUUID().toString(),
            userImageData = "https://randomuser.me/api/portraits/women/11.jpg",
            userName = "Melisa Arı",
            postData = "https://images.pexels.com/photos/3183197/pexels-photo-3183197.jpeg",
            postRatio = (6000f / 4004f),
            isLiked = true,
            likedCount = 87,
            commentCount = 19,
            likedUserImageDataList = listOf(
                "https://randomuser.me/api/portraits/men/18.jpg",
                "https://randomuser.me/api/portraits/women/30.jpg",
                "https://randomuser.me/api/portraits/men/45.jpg",
            ),
            likedUserNameList = listOf("Burak", "Nazlı", "Mert"),
            commentPreviewList = listOf(
                "Melisa Arı" to "Çok profesyonel görünüyor.",
                "Burak" to "Favorilerime ekledim.",
                "Nazlı" to "Bunu basıp duvara asardım.",
            ),
            time = "1 gün önce",
        ),
        MockDashboard(
            uuid = UUID.randomUUID().toString(),
            userImageData = "https://randomuser.me/api/portraits/men/23.jpg",
            userName = "Kerem Demir",
            postData = "https://images.pexels.com/photos/210186/pexels-photo-210186.jpeg",
            postRatio = (2200f / 1331f),
            isLiked = false,
            likedCount = 0,
            commentCount = 1,
            likedUserImageDataList = listOf(),
            likedUserNameList = listOf(),
            commentPreviewList = listOf(
                "Kerem Demir" to "Güzelmiş, neresi burası?",
            ),
            time = "1 gün önce",
        ),
    )
}
