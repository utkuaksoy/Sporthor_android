//
//  SplashBuilder.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//
//

import Foundation

final class SplashBuilder {
    static func build() -> SplashViewController {
        let view = SplashViewController()
        let interactor = SplashInteractor()
        let router = SplashRouter(viewController: view)
        let presenter = SplashPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
