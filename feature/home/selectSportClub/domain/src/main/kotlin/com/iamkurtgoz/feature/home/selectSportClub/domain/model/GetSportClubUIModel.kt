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
package com.iamkurtgoz.feature.home.selectSportClub.domain.model

import com.iamkurtgoz.domain.model.response.GetSportClubDomainModelBranch

data class GetSportClubUIModel(
    val address: String?,
    val city: String?,
    val clubId: String?,
    val clubName: String?,
    val confirmationStatus: Int?,
    val county: String?,
    val foundationYear: String?,
    val logo: String?,
    val branch: GetSportClubDomainModelBranch?,
)

val mockSportClubList = listOf(
    GetSportClubUIModel(
        address = "Fenerbahçe Spor Kulübü, Kadıköy, İstanbul, Türkiye",
        city = "İstanbul",
        clubId = "68658a727d304bb096270d4c",
        clubName = "Fenerbahçe SK",
        confirmationStatus = 0,
        county = "Kadıköy",
        foundationYear = "1907",
        logo = "https://api.sporthor.com/Uploads/1dd57c11-631f-4161-82d3-f8d93dcc04f9.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Fenerbahçe Can Bartu Tesisleri, Fenerbahçe Spor Tesisleri, Sancaktepe, İstanbul, 34885, Türkiye",
        city = "İstanbul",
        clubId = "6865b97368299bfae47dae34",
        clubName = "Fenerbahçe SK Kadın Voleybol",
        confirmationStatus = 0,
        county = "Sancaktepe",
        foundationYear = "",
        logo = "https://api.sporthor.com/Uploads/fa504726-cb86-42dd-b0e4-c55d90874e8b.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 7895",
        city = "Test Sport Club 9312",
        clubId = "6869385b6f5879bd3d0a35a5",
        clubName = "Test Sport Club 9071",
        confirmationStatus = 0,
        county = "Test Sport Club 9569",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/47ad3bac-e9a4-4f07-b51c-de0b9163df67.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Señor Sisig, Stockton St, 1, San Francisco, CA, 94108, United States",
        city = "CA",
        clubId = "686938706f5879bd3d0a35a6",
        clubName = "Asdasd",
        confirmationStatus = 0,
        county = "San Francisco",
        foundationYear = "",
        logo = "https://api.sporthor.com/Uploads/d4bd6502-105e-412d-92bc-62ca665b59ba.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Embassy English, Market St, 800, San Francisco, CA, 94102, United States",
        city = "CA",
        clubId = "6869388a6f5879bd3d0a35a7",
        clubName = "Asdasd",
        confirmationStatus = 0,
        county = "San Francisco",
        foundationYear = "",
        logo = "https://api.sporthor.com/Uploads/0e2d89c2-b1ab-4e38-b020-bc3b724ac71d.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 5204",
        city = "Test Sport Club 4488",
        clubId = "686938976f5879bd3d0a35a8",
        clubName = "Test Sport Club 4274",
        confirmationStatus = 0,
        county = "Test Sport Club 5594",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/b5b2aedf-706a-4095-b50a-c5048361e009.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Annie's Hot Dogs & Pretzels, Market St, 800, San Francisco, CA, 94102, United States",
        city = "CA",
        clubId = "686938ab6f5879bd3d0a35a9",
        clubName = "Asdasdad",
        confirmationStatus = 0,
        county = "San Francisco",
        foundationYear = "",
        logo = "https://api.sporthor.com/Uploads/918c05c6-a994-4336-bafd-4ba19b58ab6c.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 2635",
        city = "Test Sport Club 2485",
        clubId = "686938c46f5879bd3d0a35aa",
        clubName = "Test Sport Club 4723",
        confirmationStatus = 0,
        county = "Test Sport Club 9538",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/ab00f5ef-3871-4665-89c7-f4971ec4a850.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 1054",
        city = "Test Sport Club 7289",
        clubId = "68693a756f5879bd3d0a35ab",
        clubName = "Test Sport Club 1984",
        confirmationStatus = 0,
        county = "Test Sport Club 9313",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/779d335c-7554-47ce-bd04-71ccd9158fe5.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 5131",
        city = "Test Sport Club 3987",
        clubId = "68693c446f5879bd3d0a35ac",
        clubName = "Test Sport Club 2747",
        confirmationStatus = 0,
        county = "Test Sport Club 6517",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/c366a6ee-ab4a-48e1-9445-29c481d60ad3.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 9459",
        city = "Test Sport Club 9849",
        clubId = "68693e366f5879bd3d0a35b2",
        clubName = "Test Sport Club 3491",
        confirmationStatus = 0,
        county = "Test Sport Club 15",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/bd808cb1-d709-496e-aba6-48c978304a84.png",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
    GetSportClubUIModel(
        address = "Test Address 8641",
        city = "Test Sport Club 6530",
        clubId = "68693e5e6f5879bd3d0a35b3",
        clubName = "Test Sport Club 9152",
        confirmationStatus = 0,
        county = "Test Sport Club 264",
        foundationYear = "2023",
        logo = "https://api.sporthor.com/Uploads/1bfd41fe-3aab-4e9c-983c-95cc20724655.jpg",
        branch = GetSportClubDomainModelBranch(
            name = "Voleybol",
            value = "asdasds",
            val2 = null,
        ),
    ),
)
