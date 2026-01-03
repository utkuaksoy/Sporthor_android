//
//  SuccessSendClubAuthorizationLetterBuilder.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

final class SuccessSendClubAuthorizationLetterBuilder {
    static func build(sportClub: SportClub) -> SuccessSendClubAuthorizationLetterViewController {
        let view = SuccessSendClubAuthorizationLetterViewController()
        let interactor = SuccessSendClubAuthorizationLetterInteractor()
        let router = SuccessSendClubAuthorizationLetterRouter(viewController: view)
        let presenter = SuccessSendClubAuthorizationLetterPresenter(view: view, interactor: interactor, router: router, sportClub: sportClub)
        view.presenter = presenter
        return view
    }
}
