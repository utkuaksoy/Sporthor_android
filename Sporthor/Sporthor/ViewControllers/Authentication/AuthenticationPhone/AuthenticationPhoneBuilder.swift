//
//  AuthenticationPhoneBuilder.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPhoneBuilder {
    static func build(isLogin: Bool) -> AuthenticationPhoneViewController {
        let view = AuthenticationPhoneViewController()
        let interactor = AuthenticationPhoneInteractor()
        let router = AuthenticationPhoneRouter(viewController: view)
        let presenter = AuthenticationPhonePresenter(view: view, interactor: interactor, router: router, isLogin: isLogin)
        view.presenter = presenter
        return view
    }
}
