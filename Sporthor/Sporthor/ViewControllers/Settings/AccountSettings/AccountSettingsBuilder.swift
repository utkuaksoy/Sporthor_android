//
//  AccountSettingsBuilder.swift
//  Sporthor
//
//  Created by derTurke on 31.07.2025.
//
//

import Foundation

final class AccountSettingsBuilder {
    static func build() -> AccountSettingsViewController {
        let view = AccountSettingsViewController()
        let interactor = AccountSettingsInteractor()
        let router = AccountSettingsRouter(viewController: view)
        let presenter = AccountSettingsPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
