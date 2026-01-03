//
//  CreateClubBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class CreateClubBuilder {
    static func build(_ model: SportClub? = nil,
                      isLogin: Bool = false) -> CreateClubViewController {
        let view = CreateClubViewController()
        let interactor = CreateClubInteractor()
        let router = CreateClubRouter(viewController: view)
        let presenter = CreateClubPresenter(view: view, interactor: interactor, router: router, model: model, isLogin: isLogin)
        view.presenter = presenter
        return view
    }
}
