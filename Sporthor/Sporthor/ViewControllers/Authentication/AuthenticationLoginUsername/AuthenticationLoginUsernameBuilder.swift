//
//  AuthenticationLoginUsernameBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.02.2025.
//
//

import Foundation

final class AuthenticationLoginUsernameBuilder {
    static func build() -> AuthenticationLoginUsernameViewController {
        let view = AuthenticationLoginUsernameViewController()
        let interactor = AuthenticationLoginUsernameInteractor()
        let router = AuthenticationLoginUsernameRouter(viewController: view)
        let presenter = AuthenticationLoginUsernamePresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
