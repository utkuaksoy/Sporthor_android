//
//  AuthenticationPersonalInformationContracts.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

protocol AuthenticationPersonalInformationPresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationPersonalInformationPresenterDelegate? { get set }
    var interactor: AuthenticationPersonalInformationInteractorProtocol { get set }
    var router: AuthenticationPersonalInformationRouterProtocol { get set }
    var name: String { get set }
    var surname: String { get set }
    var email: String { get set }
    var password: String { get set }
    var isSecureText: Bool { get set }
    
    var statusModel: StatusModel? { get set }
    
    func viewDidLoad()
    func didTappedButton(tag: Int)
    func textFieldDidEndEditing(text: String, tag: Int)
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?)
    func openTermsOfUse()
    func openPrivacyPolicy()
}

protocol AuthenticationPersonalInformationPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func updateContinueButtonTitle(_ title: String)
    func continueButtonEnabled(isEnabled: Bool)
    func prepareUI()
    func reloadData()
    func didSetFocusTextField(at indexPath: IndexPath)
    func didSetAgreementText(_ text: String)
}

protocol AuthenticationPersonalInformationInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationPersonalInformationInteractorDelegate? { get set }
}

protocol AuthenticationPersonalInformationInteractorDelegate: BaseInteractorDelegate {
}

protocol AuthenticationPersonalInformationRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationPersonalInformationRoutes)
}

enum AuthenticationPersonalInformationRoutes {
    case username(registerRequest: RegisterRequest)
    case webView(title: String, url: String)
}
