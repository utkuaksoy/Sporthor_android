//
//  ProfileEditAddBranchRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.04.2025.
//
//

import Foundation

final class ProfileEditAddBranchRouter: BaseRouter {}

// MARK: - ProfileEditAddBranchRouterProtocol
extension ProfileEditAddBranchRouter: ProfileEditAddBranchRouterProtocol {
    func handleRouter(_ router: ProfileEditAddBranchRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, model: let model):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didSelectItems(model ?? [])
            }
        }
    }
}
