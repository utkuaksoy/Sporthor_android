//
//  ListTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//
//

import Foundation

final class ListTrainingGroupBuilder {
    static func build() -> ListTrainingGroupViewController {
        let view = ListTrainingGroupViewController()
        let interactor = ListTrainingGroupInteractor()
        let router = ListTrainingGroupRouter(viewController: view)
        let presenter = ListTrainingGroupPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
