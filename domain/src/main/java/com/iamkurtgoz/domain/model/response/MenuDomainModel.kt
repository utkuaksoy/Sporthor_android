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
package com.iamkurtgoz.domain.model.response

import com.iamkurtgoz.domain.model.enums.MenuKeyType
import com.iamkurtgoz.domain.model.enums.MenuUserType

data class MenuDomainModel(
    val menu: List<MenuDomainModelItem>?,
)

data class MenuDomainModelItem(
    val menuUserType: MenuUserType?,
    val menuKey: MenuKeyType?,
    val iconPath: String?,
    val name: String?,
    val url: String?,
    val subMenus: List<MenuDomainModelSubItem>?,
    val mainMenu: Boolean?,
)

data class MenuDomainModelSubItem(
    val iconPath: String?,
    val name: String?,
    val url: String?,
    val menuKey: MenuKeyType?,
    val subMenus: List<MenuDomainModelSubItem>?,
)
