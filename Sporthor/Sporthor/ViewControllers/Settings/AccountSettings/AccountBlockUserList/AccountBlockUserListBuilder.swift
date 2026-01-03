//
//  AccountBlockUserListBuilder.swift
//  Sporthor
//
//  Created by derTurke on 15.08.2025.
//
//

import Foundation

final class AccountBlockUserListBuilder {
    static func build() -> AccountBlockUserListViewController {
        let view = AccountBlockUserListViewController()
        let interactor = AccountBlockUserListInteractor()
        let router = AccountBlockUserListRouter(viewController: view)
        let presenter = AccountBlockUserListPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
