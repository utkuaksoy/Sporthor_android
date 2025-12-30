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
package com.iamkurtgoz.feature.home.profile.profileEdit.mock.model

import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchInfoRowUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.BranchesAttributeItemUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.EditProfileInfoUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.EditProfileSummaryUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.HighlightItemResponseUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.ProfileRowUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.TeamInfoUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.model.TeamItemResponseUIModel
import com.iamkurtgoz.feature.home.profile.profileEdit.domain.types.toInfoRowType

object MockEditProfileSummaryUIModel {
    val mockEditProfileSummary = EditProfileSummaryUIModel(
        profileImage = "https://randomuser.me/api/portraits/men/30.jpg",
        profileInfo = EditProfileInfoUIModel(
            title = "Profil Bilgileri",
            row = listOf(
                ProfileRowUIModel(
                    title = "İsim, Soyisim",
                    placeholder = null,
                    text = "Ersin Terzi",
                    parameterName = "fullname",
                    isRequired = true,
                    type = 0.toInfoRowType(),
                ),
                ProfileRowUIModel(
                    title = "Kullanıcı Adı",
                    placeholder = null,
                    text = "ersinterzi",
                    parameterName = "username",
                    isRequired = true,
                    type = 0.toInfoRowType(),
                ),
                ProfileRowUIModel(
                    title = "Şehir",
                    placeholder = null,
                    text = "İstanbul",
                    parameterName = "city",
                    isRequired = true,
                    type = 0.toInfoRowType(),
                ),
                ProfileRowUIModel(
                    title = "Doğum Tarihi",
                    placeholder = null,
                    text = "31.12.2024",
                    parameterName = "birthday",
                    isRequired = true,
                    type = 1.toInfoRowType(),
                ),
                ProfileRowUIModel(
                    title = "Hakkında",
                    placeholder = null,
                    text = "Ben Bade Belgin, 2008 doğumluyum ve Eczacıbaşı Spor Kulübü U17 takımında voleybol oynuyorum. Güçlü servislerim ve bloklarım en büyük avantajlarım.",
                    parameterName = "aboutMe",
                    isRequired = true,
                    type = 0.toInfoRowType(),
                ),
                ProfileRowUIModel(
                    title = "Boy",
                    placeholder = null,
                    text = null,
                    parameterName = "length",
                    isRequired = false,
                    type = 2.toInfoRowType(),
                ),
                ProfileRowUIModel(
                    title = "Kilo",
                    placeholder = null,
                    text = null,
                    parameterName = "weight",
                    isRequired = false,
                    type = 3.toInfoRowType(),
                ),
            ),
        ),
        teamInfo = TeamInfoUIModel(
            title = "Kulüp / Takım Bilgileri",
            info = "Kulüp bilgileri antrenör ve yetkililer tarafından sağlanır.",
            teams = listOf(
                TeamItemResponseUIModel(
                    teamImage = "/uploads/teams/fenerbahce.png",
                    teamName = "Fenerbahçe",
                ),
                TeamItemResponseUIModel(
                    teamImage = "/uploads/teams/galatasaray.png",
                    teamName = "Galatasaray",
                ),
                TeamItemResponseUIModel(
                    teamImage = "/uploads/teams/galatasaray.png",
                    teamName = "Real Madrid",
                ),
            ),
        ),
        highlights = HighlightItemResponseUIModel(
            title = "Öne Çıkan Özellikler",
            branches = listOf(
                BranchItemUIModel(
                    branchImage = "",
                    branchTitle = "Voleybol",
                    branchId = "67fadfe8e23558078b7156df",
                    isSelected = true,
                ),
            ),
            branchesAttributes = listOf(
                BranchesAttributeItemUIModel(
                    branchId = "67fadfe8e23558078b7156df",
                    branchInfoRow = listOf(
                        BranchInfoRowUIModel(
                            title = "Pozisyon",
                            placeholder = "Pozisyon",
                            text = "Forvet",
                            parameterName = "position",
                            isRequired = false,
                            type = 0.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "Boy",
                            placeholder = "Boy",
                            text = "190",
                            parameterName = "length",
                            isRequired = false,
                            type = 2.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "Kilo",
                            placeholder = "Kilo",
                            text = "80",
                            parameterName = "weight",
                            isRequired = false,
                            type = 3.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "Dikey Sıçrama",
                            placeholder = "Dikey Sıçrama",
                            text = "100",
                            parameterName = "verticalJump",
                            isRequired = false,
                            type = 2.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "Bacak Uzunluğu",
                            placeholder = "Bacak Uzunluğu",
                            text = "",
                            parameterName = "legLength",
                            isRequired = false,
                            type = 2.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "Adım Uzunluğu",
                            placeholder = "Adım Uzunluğu",
                            text = "",
                            parameterName = "stepLength",
                            isRequired = false,
                            type = 2.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "5m Sprint",
                            placeholder = "5m Sprint",
                            text = "",
                            parameterName = "5mSprint",
                            isRequired = false,
                            type = 4.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "30m Sprint",
                            placeholder = "30m Sprint",
                            text = "",
                            parameterName = "30mSprint",
                            isRequired = false,
                            type = 4.toInfoRowType(),
                        ),
                        BranchInfoRowUIModel(
                            title = "Ladder Drill",
                            placeholder = "Ladder Drill",
                            text = "",
                            parameterName = "ladderDrill",
                            isRequired = false,
                            type = 5.toInfoRowType(),
                        ),
                    ),
                ),
            ),
        ),
    )
}
