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
package com.iamkurtgoz.feature.home.customizeUserInfo.domain.model

import com.iamkurtgoz.domain.model.base.Listable
import com.iamkurtgoz.feature.home.customizeUserInfo.domain.types.CustomizePageType

data class ConfigurationUIModel(
    val showExperience: Boolean?,
    val branches: List<ConfigurationBranchUIModel?>?,
    val userRoles: List<ConfigurationUserRoleUIModel?>?,
    val coachRoles: List<ConfigurationCoachRoleUIModel?>?,
    val selectedUserRolesTypeList: List<ConfigurationUserRoleUIModel?>?,
) {
    val pageList: List<CustomizePageType>
        get() {
            val list = mutableListOf<CustomizePageType>()
            if (showExperience == true) {
                list.add(CustomizePageType.Welcome)
            }
            list.add(CustomizePageType.UserBranchPage)
            list.add(CustomizePageType.UserRoleCategory)
            if (selectedUserRolesTypeList?.any { it?.name == "Antranör" } == true) {
                list.add(CustomizePageType.CoachRoleCategory)
            }
            list.add(CustomizePageType.UserInfo)
            return list
        }
}

data class ConfigurationBranchUIModel(
    val detail: String?,
    val name: String?,
    val value: String?,
    override val uuid: String? = name + value + detail,
) : Listable

data class ConfigurationUserRoleUIModel(
    val detail: String?,
    val name: String?,
    val value: String?,
    override val uuid: String? = name + value + detail,
) : Listable

data class ConfigurationCoachRoleUIModel(
    val detail: String?,
    val name: String?,
    val value: String?,
    override val uuid: String? = name + value + detail,
) : Listable
