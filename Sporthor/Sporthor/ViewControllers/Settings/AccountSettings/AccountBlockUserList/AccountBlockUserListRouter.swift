//
//  AccountBlockUserListRouter.swift
//  Sporthor
//
//  Created by derTurke on 15.08.2025.
//
//

import Foundation

final class AccountBlockUserListRouter: BaseRouter {}

// MARK: - AccountBlokeListRouterProtocol
extension AccountBlockUserListRouter: AccountBlockUserListRouterProtocol {
    func handleRouter(_ router: AccountBlockUserListRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
