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
import com.iamkurtgoz.data.model.MenuResponseModel
import com.iamkurtgoz.data.model.MenuResponseModelItem
import com.iamkurtgoz.data.model.MenuResponseModelSubItem
import com.iamkurtgoz.domain.model.enums.MenuKeyType
import com.iamkurtgoz.domain.model.enums.MenuUserType
import com.iamkurtgoz.domain.model.response.MenuDomainModel
import com.iamkurtgoz.domain.model.response.MenuDomainModelItem
import com.iamkurtgoz.domain.model.response.MenuDomainModelSubItem
import javax.inject.Inject

internal class MenuDomainMapper @Inject constructor(
    private val menuItemDomainMapper: MenuItemDomainMapper,
) : IMapper<MenuResponseModel, MenuDomainModel> {

    override fun map(response: MenuResponseModel): MenuDomainModel {
        return MenuDomainModel(
            menu = response.menu?.map(menuItemDomainMapper::map),
        )
    }
}

internal class MenuItemDomainMapper @Inject constructor(
    private val subMenuItemDomainMapper: SubMenuItemDomainMapper,
) : IMapper<MenuResponseModelItem, MenuDomainModelItem> {

    override fun map(response: MenuResponseModelItem): MenuDomainModelItem {
        return with(response) {
            MenuDomainModelItem(
                menuUserType = MenuUserType.from(menuUserType),
                menuKey = MenuKeyType.from(menuKey),
                iconPath = iconPath,
                name = name,
                url = url,
                subMenus = subMenus
                    ?.filterNotNull()
                    ?.map(subMenuItemDomainMapper::map),
                mainMenu = mainMenu,
            )
        }
    }
}

internal class SubMenuItemDomainMapper @Inject constructor() : IMapper<MenuResponseModelSubItem, MenuDomainModelSubItem> {

    override fun map(response: MenuResponseModelSubItem): MenuDomainModelSubItem {
        return with(response) {
            MenuDomainModelSubItem(
                iconPath = iconPath,
                name = name,
                url = url,
                menuKey = MenuKeyType.from(menuKey),
                subMenus = subMenus
                    ?.filterNotNull()
                    ?.map { map(it) },
            )
        }
    }
}
