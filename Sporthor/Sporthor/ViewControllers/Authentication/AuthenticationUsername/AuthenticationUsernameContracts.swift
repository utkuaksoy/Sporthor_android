//
//  AuthenticationUsernameContracts.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

protocol AuthenticationUsernamePresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationUsernamePresenterDelegate? { get set }
    var interactor: AuthenticationUsernameInteractorProtocol { get set }
    var router: AuthenticationUsernameRouterProtocol { get set }
    var username: String { get set }
    var checkUsernameResponse: CheckUsernameResponse? { get set }
    var alertDescription: String { get set }
    var isValidateUsername: Bool { get set }
    var isTrueUsername: Bool { get set }
    
    func viewDidLoad()
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath)
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath)
    func didSelectUsername(_ text: String)
    func didTappedContinueButton()
}

protocol AuthenticationUsernamePresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func updateContinueButtonTitle(_ title: String)
    func continueButtonEnabled(isEnabled: Bool)
    func prepareUI()
    func reloadData()
    func didSetFocusTextField(at indexPath: IndexPath)
}

protocol AuthenticationUsernameInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationUsernameInteractorDelegate? { get set }
    
    func checkUsername(_ request: [String: Any]) async
    func register(_ request: [String: Any]) async
    func getConfiguration() async
}

protocol AuthenticationUsernameInteractorDelegate: BaseInteractorDelegate {
    func didCheckUsername(_ response: CheckUsernameResponse)
    func didRegister(_ response: AuthResponse)
    func didGetConfiguration(_ response: GetConfigurationResponse)
}

protocol AuthenticationUsernameRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationUsernameRoutes)
}

enum AuthenticationUsernameRoutes {
    case experienceMain
}
