//
//  AuthenticationLoginUsernameRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.02.2025.
//
//

import Foundation

final class AuthenticationLoginUsernameRouter: BaseRouter {}

// MARK: - AuthenticationLoginUsernameRouterProtocol
extension AuthenticationLoginUsernameRouter: AuthenticationLoginUsernameRouterProtocol {
    func handleRouter(_ router: AuthenticationLoginUsernameRoutes) {
        switch router {
        case .openForgotPassword(let delegate):
            let vc = AuthenticationForgotPasswordBuilder.build(delegate: delegate)
            viewController.show(vc, sender: nil)
        case .home:
            let vc = CustomTabBarController()
            vc.modalTransitionStyle = .crossDissolve
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: true)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .register:
            let vc = AuthenticationPhoneBuilder.build(isLogin: false)
            viewController.show(vc, sender: nil)
        }
    }
}
