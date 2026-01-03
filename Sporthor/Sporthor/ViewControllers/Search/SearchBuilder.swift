//
//  SearchBuilder.swift
//  Sporthor
//
//  Created by derTurke on 7.03.2025.
//
//

import Foundation

final class SearchBuilder {
    static func build() -> SearchViewController {
        let view = SearchViewController()
        let interactor = SearchInteractor()
        let router = SearchRouter(viewController: view)
        let presenter = SearchPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
