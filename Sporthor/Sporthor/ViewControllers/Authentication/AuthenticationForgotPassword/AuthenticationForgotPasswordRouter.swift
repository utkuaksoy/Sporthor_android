//
//  AuthenticationForgotPasswordRouter.swift
//  Sporthor
//
//  Created by derTurke on 20.02.2025.
//
//

import Foundation

final class AuthenticationForgotPasswordRouter: BaseRouter {}

// MARK: - AuthenticationForgotPasswordRouterProtocol
extension AuthenticationForgotPasswordRouter: AuthenticationForgotPasswordRouterProtocol {
    func handleRouter(_ router: AuthenticationForgotPasswordRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
