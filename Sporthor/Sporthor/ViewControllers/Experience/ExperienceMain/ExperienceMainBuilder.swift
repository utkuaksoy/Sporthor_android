//
//  ExperienceMainBuilder.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceMainBuilder {
    static func build() -> ExperienceMainViewController {
        let view = ExperienceMainViewController()
        let interactor = ExperienceMainInteractor()
        let router = ExperienceMainRouter(viewController: view)
        let presenter = ExperienceMainPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
