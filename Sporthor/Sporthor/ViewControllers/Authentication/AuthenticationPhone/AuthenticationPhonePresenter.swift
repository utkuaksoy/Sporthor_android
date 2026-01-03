//
//  AuthenticationPhonePresenter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation
import CommonKit

final class AuthenticationPhonePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationPhonePresenterDelegate? {
        get { return self.baseView as? AuthenticationPhonePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationPhoneInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationPhoneInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationPhoneRouterProtocol {
        get { return self.baseRouter as! AuthenticationPhoneRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationPhonePresenterDelegate,
         interactor: AuthenticationPhoneInteractorProtocol,
         router: AuthenticationPhoneRouterProtocol,
         isLogin: Bool) {
        self.isLogin = isLogin
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    var isLogin: Bool
    var numberOfItemsInSection: Int = 0 {
        didSet {
            view?.reloadData()
        }
    }
    private var phoneNumber: String?
    private var areaCode: String?
}

// MARK: - AuthenticationPhonePresenterProtocol
extension AuthenticationPhonePresenter: AuthenticationPhonePresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        changePage()
    }
    
    private func navigate(_ routes: AuthenticationPhoneRoutes) {
        router.handleRouter(routes)
    }
    
    private func changePage() {
        setTitleAndDescriptionText()
        setNumberOfItemsInSection()
        view?.updateContinueButtonAppearance(
            title: isLogin ? "" : DesignKitL10n.Authentication.Phone.Register.buttonTitle,
            isEnabled: isLogin,
            isHidden: isLogin)
        view?.reloadData()
    }
    
    private func setTitleAndDescriptionText() {
        view?.didSetTitleAndDescriptionText(
            isLogin ? DesignKitL10n.Authentication.Phone.Login.title : DesignKitL10n.Authentication.Phone.Register.title,
            isLogin ? "" : DesignKitL10n.Authentication.Phone.Register.description)
    }
    
    private func setNumberOfItemsInSection() {
        numberOfItemsInSection = isLogin ? 8 : 2
    }
    
    func didTappedButton(tag: Int) {
        switch tag {
        case 0:
            // Register Doğrulama Kodu
            generateOtp()
        case 1:
            // Login Giriş Yap
            generateOtp()
        case 2:
            // Register Giriş Yap
            Task {
                @MainActor in
                if BaseHelper.shared.navigationControllerToContains(with: AuthenticationLoginUsernameViewController.self) {
                    navigate(.back)
                } else {
                    navigate(.openLogin)
                }
            }
        case 3:
            // Giriş
            navigate(.openLogin)
        case 4:
            // Google Giriş
            break;
        case 5:
            // Facebook Giriş
            break;
        case 6:
            // Apple Giriş
            break;
        case 7:
            // Login Kayıt Ol
            isLogin = false
            changePage()
        default:
            break
        }
    }
    
    func didChangeText(_ text: String, tag: Int) {
        switch tag {
        case 0:
            areaCode = text
        case 1:
            phoneNumber = text
            guard let phoneNumber else {
                view?.continueButtonEnabled(isEnabled: false)
                return
            }
            
            if !isLogin && !phoneNumber.isEmpty && phoneNumber.count >= 10 {
                view?.continueButtonEnabled(isEnabled: true)
            } else if !isLogin {
                view?.continueButtonEnabled(isEnabled: false)
            }
        default:
            break
        }
    }
    
    private func generateOtp() {
        guard let areaCode, let phoneNumber, phoneNumber.count == 10 else { return }
        let mergePhoneText: String = areaCode + phoneNumber
        Task {
            @MainActor in
            await interactor.generateOtp(request: ["mobilePhone": mergePhoneText])
        }
    }
    
    private func openVerifyCode() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            guard let areaCode, let phoneNumber, phoneNumber.count == 10 else { return }
            navigate(.openAuthenticationVerifyCode(isLogin: isLogin, areaCode: areaCode, phoneNumber: phoneNumber))
        }
    }
}

// MARK: - AuthenticationPhoneInteractorDelegate
extension AuthenticationPhonePresenter: AuthenticationPhoneInteractorDelegate {
    func didGenerateOtp(_ response: GenerateOtpResponse) {
        openVerifyCode()
    }
}
