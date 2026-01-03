//
//  AuthenticationVerifyCodeBuilder.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationVerifyCodeBuilder {
    static func build(isLogin: Bool, areaCode: String, phoneNumber: String) -> AuthenticationVerifyCodeViewController {
        let view = AuthenticationVerifyCodeViewController()
        let interactor = AuthenticationVerifyCodeInteractor()
        let router = AuthenticationVerifyCodeRouter(viewController: view)
        let presenter = AuthenticationVerifyCodePresenter(view: view,
                                                          interactor: interactor,
                                                          router: router,
                                                          isLogin: isLogin,
                                                          areaCode: areaCode,
                                                          phoneNumber: phoneNumber)
        view.presenter = presenter
        return view
    }
}
