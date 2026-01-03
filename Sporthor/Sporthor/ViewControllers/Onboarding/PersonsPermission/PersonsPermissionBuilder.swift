//
//  PersonsPermissionBuilder.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class PersonsPermissionBuilder {
    static func build() -> PersonsPermissionViewController {
        let view = PersonsPermissionViewController()
        let interactor = PersonsPermissionInteractor()
        let router = PersonsPermissionRouter(viewController: view)
        let presenter = PersonsPermissionPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
