//
//  MyTeamCoachesDetailBuilder.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

final class MyTeamCoachesDetailBuilder {
    static func build(_ model: GetClubsAndDetailClub) -> MyTeamCoachesDetailViewController {
        let view = MyTeamCoachesDetailViewController()
        let interactor = MyTeamCoachesDetailInteractor()
        let router = MyTeamCoachesDetailRouter(viewController: view)
        let presenter = MyTeamCoachesDetailPresenter(view: view,
                                                     interactor: interactor,
                                                     router: router,
                                                     model: model)
        view.presenter = presenter
        return view
    }
}
