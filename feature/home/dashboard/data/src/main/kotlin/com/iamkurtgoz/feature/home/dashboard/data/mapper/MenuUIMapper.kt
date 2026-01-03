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
package com.iamkurtgoz.feature.home.dashboard.data.mapper

import com.iamkurtgoz.core.common.mapper.IMapper
import com.iamkurtgoz.domain.model.response.MenuDomainModel
import com.iamkurtgoz.domain.model.response.MenuDomainModelItem
import com.iamkurtgoz.domain.model.response.MenuDomainModelSubItem
import com.iamkurtgoz.feature.home.dashboard.domain.model.MenuUIModel
import com.iamkurtgoz.feature.home.dashboard.domain.model.MenuUIModelItem
import com.iamkurtgoz.feature.home.dashboard.domain.model.MenuUIModelSubItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MenuUIMapper @Inject constructor(
    private val menuItemUIMapper: MenuItemUIMapper,
) : IMapper<MenuDomainModel, MenuUIModel> {

    override fun map(response: MenuDomainModel): MenuUIModel {
        return MenuUIModel(
            menu = response.menu?.map(menuItemUIMapper::map),
        )
    }
}

internal class MenuItemUIMapper @Inject constructor(
    private val subMenuItemUIMapper: SubMenuItemUIMapper,
) : IMapper<MenuDomainModelItem, MenuUIModelItem> {

    override fun map(response: MenuDomainModelItem): MenuUIModelItem {
        return with(response) {
            MenuUIModelItem(
                menuUserType = menuUserType,
                menuKey = menuKey,
                iconPath = iconPath,
                name = name,
                url = url,
                mainMenu = mainMenu,
                subMenus = subMenus
                    ?.map(subMenuItemUIMapper::map), // tüm tree buradan başlıyor
            )
        }
    }
}

internal class SubMenuItemUIMapper @Inject constructor() :
    IMapper<MenuDomainModelSubItem, MenuUIModelSubItem> {

    override fun map(response: MenuDomainModelSubItem): MenuUIModelSubItem {
        return with(response) {
            MenuUIModelSubItem(
                iconPath = iconPath,
                name = name,
                url = url,
                menuKey = menuKey,
                subMenus = subMenus?.map { map(it) }, // REKÜRSİF
            )
        }
    }
}
