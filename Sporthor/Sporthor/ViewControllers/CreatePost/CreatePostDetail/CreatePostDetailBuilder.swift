//
//  CreatePostDetailBuilder.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//
//

import Foundation

final class CreatePostDetailBuilder {
    static func build(_ model: [AssetModel] = []) -> CreatePostDetailViewController {
        let view = CreatePostDetailViewController()
        let interactor = CreatePostDetailInteractor()
        let router = CreatePostDetailRouter(viewController: view)
        let presenter = CreatePostDetailPresenter(view: view, interactor: interactor, router: router, model: model)
        view.presenter = presenter
        return view
    }
}
