//
//  NextOnboardingRouter.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class NextOnboardingRouter: BaseRouter {}

// MARK: - NextOnboardingRouterProtocol
extension NextOnboardingRouter: NextOnboardingRouterProtocol {
    func handleRouter(_ router: NextOnboardingRoutes) {
        switch router {
        case .authenticationTeam:
            let vc = AuthenticationTeamBuilder.build()
            viewController.show(vc, sender: nil)
        case .home:
            let vc = CustomTabBarController()
            viewController.show(vc, sender: nil)
        case .personsPermission:
            let vc = PersonsPermissionBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
