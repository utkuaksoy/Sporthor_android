//
//  AuthenticationUsernameBuilder.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationUsernameBuilder {
    static func build(registerRequest: RegisterRequest) -> AuthenticationUsernameViewController {
        let view = AuthenticationUsernameViewController()
        let interactor = AuthenticationUsernameInteractor()
        let router = AuthenticationUsernameRouter(viewController: view)
        let presenter = AuthenticationUsernamePresenter(view: view,
                                                        interactor: interactor,
                                                        router: router,
                                                        registerRequest: registerRequest)
        view.presenter = presenter
        return view
    }
}
