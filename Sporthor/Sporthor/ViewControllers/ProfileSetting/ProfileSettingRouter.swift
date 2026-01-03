//
//  ProfileSettingRouter.swift
//  Sporthor
//
//  Created by derTurke on 16.08.2025.
//
//

import Foundation

final class ProfileSettingRouter: BaseRouter {}

// MARK: - ProfileSettingRouterProtocol
extension ProfileSettingRouter: ProfileSettingRouterProtocol {
    func handleRouter(_ router: ProfileSettingRoutes) {
        switch router {
        case .blockUser(delegate: let delegate):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didBlockUser()
            }
        }
    }
}
