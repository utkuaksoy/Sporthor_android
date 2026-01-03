//
//  ExperienceBirthdateAndGenderRouter.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBirthdateAndGenderRouter: BaseRouter {}

// MARK: - ExperienceBirthdateAndGenderRouterProtocol
extension ExperienceBirthdateAndGenderRouter: ExperienceBirthdateAndGenderRouterProtocol {
    func handleRouter(_ router: ExperienceBirthdateAndGenderRoutes) {
        switch router {
        case .nextOnboarding:
            let vc = NextOnboardingBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
