//
//  ListClubBuilder.swift
//  Sporthor
//
//  Created by derTurke on 3.07.2025.
//
//

import Foundation

final class ListClubBuilder {
    static func build() -> ListClubViewController {
        let view = ListClubViewController()
        let interactor = ListClubInteractor()
        let router = ListClubRouter(viewController: view)
        let presenter = ListClubPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
