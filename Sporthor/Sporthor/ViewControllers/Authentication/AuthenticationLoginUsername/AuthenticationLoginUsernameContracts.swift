//
//  AuthenticationLoginUsernameContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.02.2025.
//
//

import Foundation

protocol AuthenticationLoginUsernamePresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationLoginUsernamePresenterDelegate? { get set }
    var interactor: AuthenticationLoginUsernameInteractorProtocol { get set }
    var router: AuthenticationLoginUsernameRouterProtocol { get set }
    var username: String { get set }
    var password: String { get set }
    var isSecureText: Bool { get set }
    
    func viewDidLoad()
    func didTappedTextFieldImage(tag: Int, indexPath: IndexPath?)
    func didTextFieldEndEditing(text: String, tag: Int)
    func didTappedCellButton(tag: Int)
    func didTappedContinueButton()
    func didTappedCKHorizontalButton(tag: Int)
}

protocol AuthenticationLoginUsernamePresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescription(title: String, description: String)
    func didSetContinueButtonTitle(_ title: String)
    func prepareUI()
    func reloadData()
    func updateContinueButtonEnabled(_ isEnabled: Bool)
    func didSetFocusTextField(at indexPath: IndexPath)
}

protocol AuthenticationLoginUsernameInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationLoginUsernameInteractorDelegate? { get set }
    func loginWithUsername(_ request: [String: Any]) async
    func getConfiguration() async
}

protocol AuthenticationLoginUsernameInteractorDelegate: BaseInteractorDelegate {
    func didLoginWithUsername(_ response: AuthResponse)
    func didGetConfiguration(_ response: GetConfigurationResponse)
}

protocol AuthenticationLoginUsernameRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationLoginUsernameRoutes)
}

enum AuthenticationLoginUsernameRoutes {
    case openForgotPassword(delegate: AuthenticationForgotPasswordViewDelegate?)
    case home
    case back
    case register
}
