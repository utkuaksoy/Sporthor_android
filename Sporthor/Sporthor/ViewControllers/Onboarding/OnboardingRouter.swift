//
//  OnboardingRouter.swift
//  Sporthor
//
//  Created by derTurke on 6.02.2025.
//
//

import UIKit

final class OnboardingRouter: BaseRouter {}

// MARK: - OnboardingRouterProtocol
extension OnboardingRouter: OnboardingRouterProtocol {
    func handleRouter(_ router: OnboardingRoutes) {
        switch router {
        case .register:
            let vc = AuthenticationPhoneBuilder.build(isLogin: false)
            viewController.show(vc, sender: nil)
        case .login:
            let vc = AuthenticationLoginUsernameBuilder.build()
            viewController.show(vc, sender: nil)
        case .discover:
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = scene.windows.first {
                let tabBarController = CustomTabBarController()
                window.rootViewController = tabBarController
                window.backgroundColor = .white
                window.makeKeyAndVisible()
            }
        }
    }
}
