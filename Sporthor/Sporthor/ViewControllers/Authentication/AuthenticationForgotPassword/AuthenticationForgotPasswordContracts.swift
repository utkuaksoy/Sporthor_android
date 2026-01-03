//
//  AuthenticationForgotPasswordContracts.swift
//  Sporthor
//
//  Created by derTurke on 20.02.2025.
//
//

import Foundation

protocol AuthenticationForgotPasswordPresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationForgotPasswordPresenterDelegate? { get set }
    var interactor: AuthenticationForgotPasswordInteractorProtocol { get set }
    var router: AuthenticationForgotPasswordRouterProtocol { get set }
    var username: String { get set }
    var linkTypes: [String] { get set }
    var selectedLinkType: Int? { get set }
    
    func viewDidLoad()
    func textFieldDidEndEditing(_ text: String, tag: Int)
    func selectLinkType(_ index: Int)
    func didTappedContinueButton()
}

protocol AuthenticationForgotPasswordPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func updateContinueButtonTitle(_ title: String)
    func prepareUI()
    func reloadData()
    func continueButtonEnabled(isEnabled: Bool)
}

protocol AuthenticationForgotPasswordInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationForgotPasswordInteractorDelegate? { get set }
    func forgotPassword(_ request: [String: Any]) async
}

protocol AuthenticationForgotPasswordInteractorDelegate: BaseInteractorDelegate {
    func didForgotPassword()
}

protocol AuthenticationForgotPasswordRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationForgotPasswordRoutes)
}

enum AuthenticationForgotPasswordRoutes {
    case back
}

protocol AuthenticationForgotPasswordViewDelegate: AnyObject {
    func reloadViewData()
}

extension AuthenticationForgotPasswordViewDelegate {
    func reloadViewData() {}
}
