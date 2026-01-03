//
//  SplashRouter.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//
//

import Factory
import UIKit

final class SplashRouter: BaseRouter {}

// MARK: - SplashRouterProtocol
extension SplashRouter: SplashRouterProtocol {
    func handleRouter(_ router: SplashRoutes) {
        switch router {
        case .onboarding:
            let vc = OnboardingBuilder.build()
            viewController.show(vc, sender: nil)
        case .tabbar:
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
