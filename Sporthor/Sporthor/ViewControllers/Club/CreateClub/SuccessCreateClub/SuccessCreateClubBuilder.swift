//
//  SuccessCreateClubBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessCreateClubBuilder {
    static func build(delegate: SuccessCreateClubDelegate? = nil,
                      sportClub: SportClub? = nil,
                      infoTitle: String = "",
                      infoDescription: String = "") -> SuccessCreateClubViewController {
        let view = SuccessCreateClubViewController()
        let interactor = SuccessCreateClubInteractor()
        let router = SuccessCreateClubRouter(viewController: view)
        let presenter = SuccessCreateClubPresenter(view: view,
                                                   interactor: interactor,
                                                   router: router,
                                                   delegate: delegate,
                                                   sportClub: sportClub,
                                                   infoTitle: infoTitle,
                                                   infoDescription: infoDescription)
        view.presenter = presenter
        return view
    }
}
