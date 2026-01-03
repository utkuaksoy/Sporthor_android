//
//  AuthenticationForgotPasswordPresenter.swift
//  Sporthor
//
//  Created by derTurke on 20.02.2025.
//
//

import Foundation

final class AuthenticationForgotPasswordPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationForgotPasswordPresenterDelegate? {
        get { return self.baseView as? AuthenticationForgotPasswordPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationForgotPasswordInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationForgotPasswordInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationForgotPasswordRouterProtocol {
        get { return self.baseRouter as! AuthenticationForgotPasswordRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationForgotPasswordPresenterDelegate,
         interactor: AuthenticationForgotPasswordInteractorProtocol,
         router: AuthenticationForgotPasswordRouterProtocol,
         delegate: AuthenticationForgotPasswordViewDelegate?) {
        self.forgotPasswordDelegate = delegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    weak var forgotPasswordDelegate: AuthenticationForgotPasswordViewDelegate?
    var username: String = ""
    var linkTypes: [String] = []
    var selectedLinkType: Int?
}

// MARK: - AuthenticationForgotPasswordPresenterProtocol
extension AuthenticationForgotPasswordPresenter: AuthenticationForgotPasswordPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitleAndDescriptionText(DesignKitL10n.Authentication.ForgotPassword.title,
                                            DesignKitL10n.Authentication.ForgotPassword.description)
        view?.updateContinueButtonTitle(DesignKitL10n.Authentication.ForgotPassword.buttonTitle)
        view?.prepareUI()
        
        prepareLinkTypes()
    }
    
    private func navigate(_ routes: AuthenticationForgotPasswordRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func prepareLinkTypes() {
        linkTypes = ["Mail", "SMS"]
        view?.reloadData()
    }
    
    func textFieldDidEndEditing(_ text: String, tag: Int) {
        username = text
        checkButtonState()
    }
    
    private func checkButtonState() {
        guard !username.isEmpty,
              username.isValidateUsername(),
              let _ = selectedLinkType else {
            view?.continueButtonEnabled(isEnabled: false)
            return
        }
        view?.continueButtonEnabled(isEnabled: true)
    }
    
    func selectLinkType(_ index: Int) {
        selectedLinkType = index
        checkButtonState()
        view?.reloadData()
    }
    
    func didTappedContinueButton() {
        let request: [String: Any] = ["username": username,
                                      "type": selectedLinkType]
        Task {
            @MainActor in
            await interactor.forgotPassword(request)
        }
    }
}

// MARK: - AuthenticationForgotPasswordInteractorDelegate
extension AuthenticationForgotPasswordPresenter: AuthenticationForgotPasswordInteractorDelegate {
    func didForgotPassword() {
        var title: String = ""
        var message: String = ""
        
        if selectedLinkType == 0 {
            title = "Sıfırlama linki mailinize gönderildi"
            message = "Sıfırlama işlemini tamamlamak için size gönderilen e-postadaki linke tıklayın. Eğer e-posta kutunuzda göremiyorsanız, spam veya gereksiz klasörünü kontrol etmeyi unutmayın."
        } else {
            title = "Sıfırlama linki sms olarak gönderildi"
            message = "Sıfırlama işlemini tamamlamak için size gönderilen sms'teki linke tıklayın."
        }
        
        showAlert(delegate: self, type: .success, title: title, message: message)
    }
}

// MARK: - AlertViewDelegate
extension AuthenticationForgotPasswordPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        navigate(.back)
        forgotPasswordDelegate?.reloadViewData()
    }
}
