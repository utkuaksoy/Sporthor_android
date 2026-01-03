//
//  AuthenticationForgotPasswordBuilder.swift
//  Sporthor
//
//  Created by derTurke on 20.02.2025.
//
//

import Foundation

final class AuthenticationForgotPasswordBuilder {
    static func build(delegate: AuthenticationForgotPasswordViewDelegate? = nil) -> AuthenticationForgotPasswordViewController {
        let view = AuthenticationForgotPasswordViewController()
        let interactor = AuthenticationForgotPasswordInteractor()
        let router = AuthenticationForgotPasswordRouter(viewController: view)
        let presenter = AuthenticationForgotPasswordPresenter(view: view,
                                                              interactor: interactor,
                                                              router: router,
                                                              delegate: delegate)
        view.presenter = presenter
        return view
    }
}
