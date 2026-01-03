//
//  CreatePostBuilder.swift
//  Sporthor
//
//  Created by derTurke on 21.04.2025.
//
//

import Foundation

final class CreatePostBuilder {
    static func build() -> CreatePostViewController {
        let view = CreatePostViewController()
        let interactor = CreatePostInteractor()
        let router = CreatePostRouter(viewController: view)
        let presenter = CreatePostPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
