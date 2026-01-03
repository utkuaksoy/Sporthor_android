//
//  AuthenticationVerifyCodeRouter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationVerifyCodeRouter: BaseRouter {}

// MARK: - AuthenticationVerifyCodeRouterProtocol
extension AuthenticationVerifyCodeRouter: AuthenticationVerifyCodeRouterProtocol {
    func handleRouter(_ router: AuthenticationVerifyCodeRoutes) {
        switch router {
        case .authenticationPersonalInformation(let registerRequest):
            let vc = AuthenticationPersonalInformationBuilder.build(registerRequest: registerRequest)
            viewController.show(vc, sender: nil)
        case .home:
            let vc = CustomTabBarController()
            vc.modalTransitionStyle = .crossDissolve
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: true)
        }
    }
}
