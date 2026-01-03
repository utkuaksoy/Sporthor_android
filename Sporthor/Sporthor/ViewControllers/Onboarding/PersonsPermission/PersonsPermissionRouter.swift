//
//  PersonsPermissionRouter.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class PersonsPermissionRouter: BaseRouter {}

// MARK: - PersonsPermissionRouterProtocol
extension PersonsPermissionRouter: PersonsPermissionRouterProtocol {
    func handleRouter(_ router: PersonsPermissionRoutes) {
        switch router {
        case .authenticationTeam:
            let vc = AuthenticationTeamBuilder.build()
            viewController.show(vc, sender: nil)
        case .home:
            let vc = CustomTabBarController()
            viewController.show(vc, sender: nil)
        }
    }
}
