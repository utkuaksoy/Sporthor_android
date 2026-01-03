//
//  ProfileEditBuilder.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//
//

import Foundation

final class ProfileEditBuilder {
    static func build(delegate: ProfileEditDelegate) -> ProfileEditViewController {
        let view = ProfileEditViewController()
        let interactor = ProfileEditInteractor()
        let router = ProfileEditRouter(viewController: view)
        let presenter = ProfileEditPresenter(view: view,
                                             interactor: interactor,
                                             router: router,
                                             delegate: delegate)
        view.presenter = presenter
        return view
    }
}
