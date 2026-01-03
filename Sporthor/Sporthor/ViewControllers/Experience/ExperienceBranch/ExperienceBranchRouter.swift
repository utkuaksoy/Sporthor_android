//
//  ExperienceBranchRouter.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBranchRouter: BaseRouter {}

// MARK: - ExperienceBranchRouterProtocol
extension ExperienceBranchRouter: ExperienceBranchRouterProtocol {
    func handleRouter(_ router: ExperienceBranchRoutes) {
        switch router {
        case .experienceBirthdateAndGender(let updateProfileRequest):
            let vc = ExperienceBirthdateAndGenderBuilder.build(updateProfileRequest: updateProfileRequest)
            viewController.show(vc, sender: nil)
        case .experienceCoachSelected(let updateProfileRequest):
            let vc = ExperienceCoachSelectedBuilder.build(updateProfileRequest: updateProfileRequest)
            viewController.show(vc, sender: nil)
        }
    }
}
