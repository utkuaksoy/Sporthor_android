//
//  MenuBuilder.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

final class MenuBuilder {
    static func build(menu: [MenuModel] = [],
                      isSubMenu: Bool = false,
                      title: String = "") -> MenuViewController {
        let view = MenuViewController()
        let interactor = MenuInteractor()
        let router = MenuRouter(viewController: view)
        let presenter = MenuPresenter(view: view,
                                      interactor: interactor,
                                      router: router,
                                      menu: menu,
                                      isSubMenu: isSubMenu,
                                      subMenuTitle: title)
        view.presenter = presenter
        return view
    }
}
