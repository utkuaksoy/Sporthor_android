//
//  AuthenticationTeamBuilder.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//
//

import Foundation

final class AuthenticationTeamBuilder {
    static func build(isLogin: Bool = false, isCoach: Bool = false, isManager: Bool = false) -> AuthenticationTeamViewController {
        let view = AuthenticationTeamViewController()
        let interactor = AuthenticationTeamInteractor()
        let router = AuthenticationTeamRouter(viewController: view)
        let presenter = AuthenticationTeamPresenter(view: view,
                                                    interactor: interactor,
                                                    router: router,
                                                    isLogin: isLogin,
                                                    isCoach: isCoach,
                                                    isManager: isManager)
        view.presenter = presenter
        return view
    }
}
