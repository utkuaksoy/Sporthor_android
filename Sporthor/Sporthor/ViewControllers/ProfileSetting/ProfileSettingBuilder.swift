//
//  ProfileSettingBuilder.swift
//  Sporthor
//
//  Created by derTurke on 16.08.2025.
//
//

import Foundation

final class ProfileSettingBuilder {
    static func build(delegate: ProfileSettingDelegate? = nil,
                      userId: String = "") -> ProfileSettingViewController {
        let view = ProfileSettingViewController()
        let interactor = ProfileSettingInteractor()
        let router = ProfileSettingRouter(viewController: view)
        let presenter = ProfileSettingPresenter(view: view,
                                                interactor: interactor,
                                                router: router,
                                                delegate: delegate,
                                                userId: userId)
        view.presenter = presenter
        return view
    }
}
