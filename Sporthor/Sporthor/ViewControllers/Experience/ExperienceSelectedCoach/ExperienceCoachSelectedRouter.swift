//
//  ExperienceCoachSelectedRouter.swift
//  Sporthor
//
//  Created by derTurke on 16.05.2025.
//
//

import Foundation

final class ExperienceCoachSelectedRouter: BaseRouter {}

// MARK: - ExperienceCoachSelectedRouterProtocol
extension ExperienceCoachSelectedRouter: ExperienceCoachSelectedRouterProtocol {
    func handleRouter(_ router: ExperienceCoachSelectedRoutes) {
        switch router {
        case .experienceBirthdateAndGender(let profileRequest):
            let vc = ExperienceBirthdateAndGenderBuilder.build(updateProfileRequest: profileRequest)
            viewController.show(vc, sender: nil)
        }
    }
}
