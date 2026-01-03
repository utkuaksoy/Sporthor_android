//
//  AboutBuilder.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//
//

import Foundation

final class AboutBuilder {
    static func build() -> AboutViewController {
        let view = AboutViewController()
        let interactor = AboutInteractor()
        let router = AboutRouter(viewController: view)
        let presenter = AboutPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
