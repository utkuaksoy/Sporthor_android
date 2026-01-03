//
//  AuthenticationLoginUsernamePresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.02.2025.
//
//

import Foundation
import CommonKit
import FirebaseMessaging
import Factory

final class AuthenticationLoginUsernamePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationLoginUsernamePresenterDelegate? {
        get { return self.baseView as? AuthenticationLoginUsernamePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationLoginUsernameInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationLoginUsernameInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationLoginUsernameRouterProtocol {
        get { return self.baseRouter as! AuthenticationLoginUsernameRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationLoginUsernamePresenterDelegate,
         interactor: AuthenticationLoginUsernameInteractorProtocol,
         router: AuthenticationLoginUsernameRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var username: String = ""
    var password: String = ""
    var isSecureText: Bool = true
}

// MARK: - AuthenticationLoginUsernamePresenterProtocol
extension AuthenticationLoginUsernamePresenter: AuthenticationLoginUsernamePresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitleAndDescription(title: DesignKitL10n.Authentication.LoginUsername.title,
                                        description: DesignKitL10n.Authentication.LoginUsername.description)
        view?.didSetContinueButtonTitle(DesignKitL10n.Authentication.LoginUsername.buttonTitle)
        view?.prepareUI()
    }
    
    private func navigate(_ routes: AuthenticationLoginUsernameRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedTextFieldImage(tag: Int, indexPath: IndexPath?) {
        isSecureText = !isSecureText
        view?.reloadData()
        guard let indexPath else { return }
        view?.didSetFocusTextField(at: indexPath)
    }
    
    func didTextFieldEndEditing(text: String, tag: Int) {
        switch tag {
        case 0:
            username = text
        case 1:
            password = text
        default:
            break
        }
        checkButtonState()
    }
    
    func checkButtonState() {
        guard !username.isEmpty, !password.isEmpty else {
            view?.updateContinueButtonEnabled(false)
            return
        }
        view?.updateContinueButtonEnabled(true)
    }
    
    func didTappedCellButton(tag: Int) {
        navigate(.openForgotPassword(delegate: self))
    }
    
    func didTappedCKHorizontalButton(tag: Int) {
        Task {
            @MainActor in
            if BaseHelper.shared.navigationControllerToContains(with: AuthenticationPhoneViewController.self) {
                navigate(.back)
            } else {
                navigate(.register)
            }
        }
        
    }
    
    func didTappedContinueButton() {
        let request: [String: Any] = ["userName": username,
                                      "password": password]
        Task {
            @MainActor in
            await interactor.loginWithUsername(request)
        }
    }
}

// MARK: - AuthenticationLoginUsernameInteractorDelegate
extension AuthenticationLoginUsernamePresenter: AuthenticationLoginUsernameInteractorDelegate {
    func didLoginWithUsername(_ response: AuthResponse) {
        ApplicationContext.shared.authResponse = response
        Messaging.messaging().token { [weak self] token, error in
            guard let self else { return }
            if let token = token {
                Container.shared.pushNotificationService().updateToken(token)
            }
            if let userRoles = ApplicationContext.shared.getConfiguration.userRoles, !userRoles.isEmpty {
                navigate(.home)
            } else {
                Task { @MainActor in
                    await self.interactor.getConfiguration()
                }
            }
        }
    }
    
    func didGetConfiguration(_ response: GetConfigurationResponse) {
        ApplicationContext.shared.getConfiguration = response
        navigate(.home)
    }
}

extension AuthenticationLoginUsernamePresenter: AuthenticationForgotPasswordViewDelegate {
    func reloadViewData() {
        password = ""
        view?.reloadData()
    }
}
