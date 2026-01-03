//
//  MyTeamCoachesBuilder.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

final class MyTeamCoachesBuilder {
    static func build() -> MyTeamCoachesViewController {
        let view = MyTeamCoachesViewController()
        let interactor = MyTeamCoachesInteractor()
        let router = MyTeamCoachesRouter(viewController: view)
        let presenter = MyTeamCoachesPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
