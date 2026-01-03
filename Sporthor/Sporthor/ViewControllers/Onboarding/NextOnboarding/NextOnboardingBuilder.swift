//
//  NextOnboardingBuilder.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class NextOnboardingBuilder {
    static func build() -> NextOnboardingViewController {
        let view = NextOnboardingViewController()
        let interactor = NextOnboardingInteractor()
        let router = NextOnboardingRouter(viewController: view)
        let presenter = NextOnboardingPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
