//
//  AuthenticationVerifyCodeContracts.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

protocol AuthenticationVerifyCodePresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationVerifyCodePresenterDelegate? { get set }
    var interactor: AuthenticationVerifyCodeInteractorProtocol { get set }
    var router: AuthenticationVerifyCodeRouterProtocol { get set }
    var againCodeIsEnabled: Bool { get set }
    
    func viewDidLoad()
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath)
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?)
    func didTappedButton(tag: Int)
    func circularCountdownViewDidFinish()
    func cellButtonClicked(tag: Int, indexPath: IndexPath?)
    func updateCode(at index: Int, with value: String)
    func checkButtonState()
}

protocol AuthenticationVerifyCodePresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func updateContinueButtonTitle(_ title: String)
    func continueButtonEnabled(isEnabled: Bool)
    func prepareUI()
    func didSetFocusTextField(at indexPath: IndexPath)
    func reloadSection(_ section: Int)
}

protocol AuthenticationVerifyCodeInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationVerifyCodeInteractorDelegate? { get set }
    func loginWithPhone(_ request: [String: Any]) async
    func validateOtp(_ request: [String: Any]) async
}

protocol AuthenticationVerifyCodeInteractorDelegate: BaseInteractorDelegate {
    func didLoginWithPhone(_ response: AuthResponse)
    func didValidateOtp()
}

protocol AuthenticationVerifyCodeRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationVerifyCodeRoutes)
}

enum AuthenticationVerifyCodeRoutes {
    case authenticationPersonalInformation(_ registerRequest: RegisterRequest)
    case home
}
