//
//  ExperienceBirthdateAndGenderBuilder.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBirthdateAndGenderBuilder {
    static func build(updateProfileRequest: UpdateProfileRequest? = nil) -> ExperienceBirthdateAndGenderViewController {
        let view = ExperienceBirthdateAndGenderViewController()
        let interactor = ExperienceBirthdateAndGenderInteractor()
        let router = ExperienceBirthdateAndGenderRouter(viewController: view)
        let presenter = ExperienceBirthdateAndGenderPresenter(view: view,
                                                              interactor: interactor,
                                                              router: router,
                                                              updateProfileRequest: updateProfileRequest)
        view.presenter = presenter
        return view
    }
}
