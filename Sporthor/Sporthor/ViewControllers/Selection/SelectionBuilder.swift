//
//  SelectionBuilder.swift
//  Sporthor
//
//  Created by derTurke on 14.04.2025.
//
//

import Foundation

final class SelectionBuilder {
    static func build(title: String = "",
                      model: [SelectionModel],
                      delegate: SelectionViewDelegate? = nil) -> SelectionViewController {
        let view = SelectionViewController()
        let interactor = SelectionInteractor()
        let router = SelectionRouter(viewController: view)
        let presenter = SelectionPresenter(view: view,
                                           interactor: interactor,
                                           router: router,
                                           title: title,
                                           model: model,
                                           delegate: delegate)
        view.presenter = presenter
        return view
    }
}
