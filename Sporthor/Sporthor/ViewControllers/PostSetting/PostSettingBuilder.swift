//
//  PostSettingBuilder.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

final class PostSettingBuilder {
    static func build(delegate: PostSettingDelegate? = nil, model: Post) -> PostSettingViewController {
        let view = PostSettingViewController()
        let interactor = PostSettingInteractor()
        let router = PostSettingRouter(viewController: view)
        let presenter = PostSettingPresenter(view: view,
                                             interactor: interactor,
                                             router: router,
                                             delegate: delegate,
                                             model: model)
        view.presenter = presenter
        return view
    }
}
