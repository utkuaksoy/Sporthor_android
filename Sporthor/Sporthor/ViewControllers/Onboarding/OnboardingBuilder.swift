//
//  OnboardingBuilder.swift
//  Sporthor
//
//  Created by derTurke on 6.02.2025.
//
//

import Foundation

final class OnboardingBuilder {
    static func build() -> OnboardingViewController {
        let view = OnboardingViewController()
        let interactor = OnboardingInteractor()
        let router = OnboardingRouter(viewController: view)
        let presenter = OnboardingPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
