//
//  AuthenticationPersonalInformationBuilder.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPersonalInformationBuilder {
    static func build(registerRequest: RegisterRequest) -> AuthenticationPersonalInformationViewController {
        let view = AuthenticationPersonalInformationViewController()
        let interactor = AuthenticationPersonalInformationInteractor()
        let router = AuthenticationPersonalInformationRouter(viewController: view)
        let presenter = AuthenticationPersonalInformationPresenter(view: view,
                                                                   interactor: interactor,
                                                                   router: router,
                                                                   registerRequest: registerRequest)
        view.presenter = presenter
        return view
    }
}
