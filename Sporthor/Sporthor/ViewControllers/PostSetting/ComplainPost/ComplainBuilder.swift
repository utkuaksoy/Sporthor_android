//
//  ComplainBuilder.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

final class ComplainBuilder {
    static func build(delegate: ComplainDelegate? = nil, model: Post) -> ComplainViewController {
        let view = ComplainViewController()
        let interactor = ComplainInteractor()
        let router = ComplainRouter(viewController: view)
        let presenter = ComplainPresenter(view: view, interactor: interactor, router: router, delegate: delegate, model: model)
        view.presenter = presenter
        return view
    }
}
