//
//  AuthenticationVerifyCodePresenter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationVerifyCodePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationVerifyCodePresenterDelegate? {
        get { return self.baseView as? AuthenticationVerifyCodePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationVerifyCodeInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationVerifyCodeInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationVerifyCodeRouterProtocol {
        get { return self.baseRouter as! AuthenticationVerifyCodeRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationVerifyCodePresenterDelegate,
         interactor: AuthenticationVerifyCodeInteractorProtocol,
         router: AuthenticationVerifyCodeRouterProtocol,
         isLogin: Bool,
         areaCode: String,
         phoneNumber: String) {
        self.isLogin = isLogin
        self.areaCode = areaCode
        self.phoneNumber = phoneNumber
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private var isLogin: Bool
    private var areaCode: String
    private var phoneNumber: String
    private var code: [String] = Array(repeating: "", count: 6)
    var againCodeIsEnabled: Bool = false
    private var registerRequest: RegisterRequest = RegisterRequest()
    
    func updateCode(at index: Int, with value: String) {
        code[index] = value
    }
    
    func checkButtonState() {
        let isComplete = !code.contains { $0.isEmpty }
        view?.continueButtonEnabled(isEnabled: isComplete)
    }
}

// MARK: - AuthenticationVerifyCodePresenterProtocol
extension AuthenticationVerifyCodePresenter: AuthenticationVerifyCodePresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        setTitleAndDescriptionText()
        view?.updateContinueButtonTitle(DesignKitL10n.Authentication.VerifyCode.buttonTitle)
    }
    
    private func setTitleAndDescriptionText() {
        let replacingDescription = DesignKitL10n.Authentication.VerifyCode.description.replacingOccurrences(of: "@", with: "\(areaCode) \(phoneNumber.formatPhoneNumber())")
        view?.didSetTitleAndDescriptionText(DesignKitL10n.Authentication.VerifyCode.title,
                                            replacingDescription)
    }
    
    private func navigate(_ routes: AuthenticationVerifyCodeRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func textFieldDidChangeSelection(text: String, tag: Int, indexPath: IndexPath) {
        var focusedIndexPath: IndexPath = indexPath
        if text.count == 1 {
            focusedIndexPath = IndexPath(item: indexPath.item + 1,
                                         section: indexPath.section)
            if indexPath.item == 2 {
                focusedIndexPath = IndexPath(item: 4, section: indexPath.section)
            }
        } else if text.isEmpty, tag > 0 {
            focusedIndexPath = IndexPath(item: indexPath.item - 1,
                                         section: indexPath.section)
            if indexPath.item == 4 {
                focusedIndexPath = IndexPath(item: 2, section: indexPath.section)
            }
        }
        view?.didSetFocusTextField(at: focusedIndexPath)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int, indexPath: IndexPath?) {
        code[tag] = text
        checkButtonState()
    }
    
    func circularCountdownViewDidFinish() {
        againCodeIsEnabled = !againCodeIsEnabled
        view?.reloadSection(2)
    }
    
    func cellButtonClicked(tag: Int, indexPath: IndexPath?) {
        againCodeIsEnabled = !againCodeIsEnabled
        view?.reloadSection(1)
        view?.reloadSection(2)
    }
    
    func didTappedButton(tag: Int) {
        callService()
    }
    
    private func callService() {
        let request: [String: Any] = ["mobilePhone": areaCode + phoneNumber,
                                      "otpCode": prepareOtpCode()]
        Task {
            @MainActor in
            if isLogin {
                await interactor.loginWithPhone(request)
            } else {
                await interactor.validateOtp(request)
            }
        }
    }
    
    private func prepareOtpCode() -> String {
        return code.joined()
    }
}

// MARK: - AuthenticationVerifyCodeInteractorDelegate
extension AuthenticationVerifyCodePresenter: AuthenticationVerifyCodeInteractorDelegate {
    func didLoginWithPhone(_ response: AuthResponse) {
        ApplicationContext.shared.authResponse = response
        navigate(.home)
    }
    
    func didValidateOtp() {
        registerRequest.mobilePhone = areaCode + phoneNumber
        navigate(.authenticationPersonalInformation(registerRequest))
    }
}
