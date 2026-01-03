//
//  AuthenticationUsernamePresenter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation
import FirebaseMessaging
import Factory

final class AuthenticationUsernamePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationUsernamePresenterDelegate? {
        get { return self.baseView as? AuthenticationUsernamePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationUsernameInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationUsernameInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationUsernameRouterProtocol {
        get { return self.baseRouter as! AuthenticationUsernameRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationUsernamePresenterDelegate,
         interactor: AuthenticationUsernameInteractorProtocol,
         router: AuthenticationUsernameRouterProtocol,
         registerRequest: RegisterRequest) {
        self.registerRequest = registerRequest
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var registerRequest: RegisterRequest
    
    var username: String = ""
    var checkUsernameResponse: CheckUsernameResponse?
    var alertDescription: String = ""
    var isValidateUsername: Bool = true
    var isTrueUsername: Bool = true
    private var isEnabledContinueButton: Bool = false
    private var usernameValidationWorkItem: DispatchWorkItem?
    private var indexPath: IndexPath = IndexPath()
}

// MARK: - AuthenticationUsernamePresenterProtocol
extension AuthenticationUsernamePresenter: AuthenticationUsernamePresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        setTitleAndDescriptionText()
        setContinueButtonTitle()
    }
    
    private func setTitleAndDescriptionText() {
        view?.didSetTitleAndDescriptionText(DesignKitL10n.Authentication.Username.title,
                                            DesignKitL10n.Authentication.Username.description)
    }
    
    private func setContinueButtonTitle() {
        view?.updateContinueButtonTitle(DesignKitL10n.Authentication.PersonalInformation.buttonTitle)
    }
    
    private func navigate(_ routes: AuthenticationUsernameRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath) {
        self.indexPath = indexPath
        guard username != text else {
            return
        }
        username = text
        usernameValidationWorkItem?.cancel()
        
        guard !username.isEmpty else { return }
        
        let workItem = DispatchWorkItem { [weak self] in
            self?.validateUsername()
        }
        usernameValidationWorkItem = workItem
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: workItem)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath) {
        self.indexPath = indexPath
        usernameValidationWorkItem?.cancel()
        username = text
        validateUsername()
    }
    
    @objc private func validateUsername() {
        guard !username.isEmpty, username.isValidateUsername() else {
            alertDescription = DesignKitL10n.Authentication.Username.regexWarning
            isTrueUsername = false
            view?.reloadData()
            return
        }
        
        checkUsername()
    }
    
    private func checkUsername() {
        let request: [String: Any] = ["username": username]
        Task {
            @MainActor in
            await interactor.checkUsername(request)
        }
    }
    
    func didSelectUsername(_ text: String) {
        usernameValidationWorkItem?.cancel()
        username = text
        validateUsername()
    }
    
    func didTappedContinueButton() {
        guard isValidateUsername else { return }
        registerRequest.username = username
        register()
    }
    
    private func register() {
        Task {
            @MainActor in
            await interactor.register(registerRequest.dictionary() ?? [:])
        }
    }
    
    private func getConfiguration() {
        Task {
            @MainActor in
            await interactor.getConfiguration()
        }
    }
}

// MARK: - AuthenticationUsernameInteractorDelegate
extension AuthenticationUsernamePresenter: AuthenticationUsernameInteractorDelegate {
    func didCheckUsername(_ response: CheckUsernameResponse) {
        checkUsernameResponse = response
        isTrueUsername = checkUsernameResponse?.isUsable ?? false
        alertDescription = isTrueUsername ? DesignKitL10n.Authentication.Username.correctStatus.replacingOccurrences(of: "@", with: username) : DesignKitL10n.Authentication.Username.wrongStatus.replacingOccurrences(of: "@", with: username)
        isValidateUsername = isTrueUsername
        view?.continueButtonEnabled(isEnabled: isTrueUsername)
        view?.reloadData()
    }
    
    func didRegister(_ response: AuthResponse) {
        ApplicationContext.shared.authResponse = response
        Messaging.messaging().token { token, error in
            if let token = token {
                Container.shared.pushNotificationService().updateToken(token)
            }
        }
        getConfiguration()
    }
    
    func didGetConfiguration(_ response: GetConfigurationResponse) {
        ApplicationContext.shared.getConfiguration = response
        navigate(.experienceMain)
    }
}
