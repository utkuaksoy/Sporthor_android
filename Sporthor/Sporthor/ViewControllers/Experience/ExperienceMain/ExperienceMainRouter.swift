//
//  ExperienceMainRouter.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceMainRouter: BaseRouter {}

// MARK: - ExperienceMainRouterProtocol
extension ExperienceMainRouter: ExperienceMainRouterProtocol {
    func handleRouter(_ router: ExperienceMainRoutes) {
        switch router {
        case .experienceJob:
            let vc = ExperienceJobBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
