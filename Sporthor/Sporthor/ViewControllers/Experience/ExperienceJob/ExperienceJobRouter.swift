//
//  ExperienceJobRouter.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceJobRouter: BaseRouter {}

// MARK: - ExperienceJobRouterProtocol
extension ExperienceJobRouter: ExperienceJobRouterProtocol {
    func handleRouter(_ router: ExperienceJobRoutes) {
        switch router {
        case .experienceBranch(let updateProfileRequest):
            let vc = ExperienceBranchBuilder.build(updateProfileRequest: updateProfileRequest)
            viewController.show(vc, sender: nil)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
