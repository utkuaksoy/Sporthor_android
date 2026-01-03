//
//  ExperienceCoachSelectedBuilder.swift
//  Sporthor
//
//  Created by derTurke on 16.05.2025.
//
//

import Foundation

final class ExperienceCoachSelectedBuilder {
    static func build(updateProfileRequest: UpdateProfileRequest? = nil) -> ExperienceCoachSelectedViewController {
        let view = ExperienceCoachSelectedViewController()
        let interactor = ExperienceCoachSelectedInteractor()
        let router = ExperienceCoachSelectedRouter(viewController: view)
        let presenter = ExperienceCoachSelectedPresenter(view: view,
                                                         interactor: interactor,
                                                         router: router,
                                                         updateProfileRequest: updateProfileRequest)
        view.presenter = presenter
        return view
    }
}
