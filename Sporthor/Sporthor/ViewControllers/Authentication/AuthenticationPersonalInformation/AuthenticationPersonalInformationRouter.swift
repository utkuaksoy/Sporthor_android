//
//  AuthenticationPersonalInformationRouter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPersonalInformationRouter: BaseRouter {}

// MARK: - AuthenticationPersonalInformationRouterProtocol
extension AuthenticationPersonalInformationRouter: AuthenticationPersonalInformationRouterProtocol {
    func handleRouter(_ router: AuthenticationPersonalInformationRoutes) {
        switch router {
        case .username(registerRequest: let registerRequest):
            let vc = AuthenticationUsernameBuilder.build(registerRequest: registerRequest)
            viewController.show(vc, sender: nil)
        case .webView(title: let title, url: let url):
            let vc = WebViewBuilder.build(title: title, url: url)
            viewController.show(vc, sender: nil)
        }
    }
}
