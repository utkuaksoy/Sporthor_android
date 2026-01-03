//
//  SettingsBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation

final class SettingsBuilder {
    static func build() -> SettingsViewController {
        let view = SettingsViewController()
        let interactor = SettingsInteractor()
        let router = SettingsRouter(viewController: view)
        let presenter = SettingsPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
