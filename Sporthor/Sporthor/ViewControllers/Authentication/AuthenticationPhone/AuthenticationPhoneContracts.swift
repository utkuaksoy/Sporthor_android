//
//  AuthenticationPhoneContracts.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

protocol AuthenticationPhonePresenterProtocol: BasePresenterProtocol {
    var view: AuthenticationPhonePresenterDelegate? { get set }
    var interactor: AuthenticationPhoneInteractorProtocol { get set }
    var router: AuthenticationPhoneRouterProtocol { get set }
    var isLogin: Bool { get set }
    var numberOfItemsInSection: Int { get set }
    
    func viewDidLoad()
    func didTappedButton(tag: Int)
    func didChangeText(_ text: String, tag: Int)
}

protocol AuthenticationPhonePresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func updateContinueButtonAppearance(title: String, isEnabled: Bool, isHidden: Bool)
    func continueButtonEnabled(isEnabled: Bool)
    func prepareUI()
    func reloadData()
}

protocol AuthenticationPhoneInteractorProtocol: BaseInteractorProtocol {
    var delegate: AuthenticationPhoneInteractorDelegate? { get set }
    
    func generateOtp(request: [String: Any]) async
}

protocol AuthenticationPhoneInteractorDelegate: BaseInteractorDelegate {
    func didGenerateOtp(_ response: GenerateOtpResponse)
}

protocol AuthenticationPhoneRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AuthenticationPhoneRoutes)
}

enum AuthenticationPhoneRoutes {
    case openAuthenticationVerifyCode(isLogin: Bool, areaCode: String, phoneNumber: String)
    case openLogin
    case back
}
