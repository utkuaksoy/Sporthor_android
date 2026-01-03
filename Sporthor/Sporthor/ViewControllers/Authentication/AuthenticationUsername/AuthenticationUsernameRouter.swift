//
//  AuthenticationUsernameRouter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationUsernameRouter: BaseRouter {}

// MARK: - AuthenticationUsernameRouterProtocol
extension AuthenticationUsernameRouter: AuthenticationUsernameRouterProtocol {
    func handleRouter(_ router: AuthenticationUsernameRoutes) {
        switch router {
        case .experienceMain:
            let vc = ExperienceMainBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
