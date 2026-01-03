//
//  AuthenticationPhoneRouter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPhoneRouter: BaseRouter {}

// MARK: - AuthenticationPhoneRouterProtocol
extension AuthenticationPhoneRouter: AuthenticationPhoneRouterProtocol {
    func handleRouter(_ router: AuthenticationPhoneRoutes) {
        switch router {
        case .openAuthenticationVerifyCode(isLogin: let isLogin,
                                           areaCode: let areaCode,
                                           phoneNumber: let phoneNumber):
            let vc = AuthenticationVerifyCodeBuilder.build(isLogin: isLogin,
                                                           areaCode: areaCode,
                                                           phoneNumber: phoneNumber)
            viewController.show(vc, sender: nil)
        case .openLogin:
            let vc = AuthenticationLoginUsernameBuilder.build()
            viewController.show(vc, sender: nil)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
