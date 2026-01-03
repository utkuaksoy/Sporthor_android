//
//  SendClubAuthorizationLetterBuilder.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

final class SendClubAuthorizationLetterBuilder {
    static func build(sportClub: SportClub) -> SendClubAuthorizationLetterViewController {
        let view = SendClubAuthorizationLetterViewController()
        let interactor = SendClubAuthorizationLetterInteractor()
        let router = SendClubAuthorizationLetterRouter(viewController: view)
        let presenter = SendClubAuthorizationLetterPresenter(view: view,
                                                             interactor: interactor,
                                                             router: router,
                                                             sportClub: sportClub)
        view.presenter = presenter
        return view
    }
}
