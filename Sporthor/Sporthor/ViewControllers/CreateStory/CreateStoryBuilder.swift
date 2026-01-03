//
//  CreateStoryBuilder.swift
//  Sporthor
//
//  Created by derTurke on 26.04.2025.
//
//

import Foundation

final class CreateStoryBuilder {
    static func build(previewDelegate: PreviewDelegate? = nil) -> CreateStoryViewController {
        let view = CreateStoryViewController()
        let interactor = CreateStoryInteractor()
        let router = CreateStoryRouter(viewController: view)
        let presenter = CreateStoryPresenter(view: view,
                                             interactor: interactor,
                                             router: router,
                                             previewDelegate: previewDelegate)
        view.presenter = presenter
        return view
    }
}
