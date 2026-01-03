//
//  AccountSettingsRouter.swift
//  Sporthor
//
//  Created by derTurke on 31.07.2025.
//
//

import Foundation

final class AccountSettingsRouter: BaseRouter {}

// MARK: - AccountSettingsRouterProtocol
extension AccountSettingsRouter: AccountSettingsRouterProtocol {
    func handleRouter(_ router: AccountSettingsRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .blockUser:
            let vc = AccountBlockUserListBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
