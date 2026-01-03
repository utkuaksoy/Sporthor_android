//
//  ProfileBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//
//

import Foundation

final class ProfileBuilder {
    static func build(userId: String? = nil, userName: String? = nil) -> ProfileViewController {
        let view = ProfileViewController()
        let interactor = ProfileInteractor(viewModel: .init())
        let router = ProfileRouter(viewController: view)
        let presenter = ProfilePresenter(
            view: view,
            interactor: interactor,
            router: router,
            userId: userId,
            userName: userName
        )
        view.presenter = presenter
        
        return view
    }
}
