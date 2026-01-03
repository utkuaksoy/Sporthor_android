//
//  AuthenticationPersonalInformationPresenter.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPersonalInformationPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AuthenticationPersonalInformationPresenterDelegate? {
        get { return self.baseView as? AuthenticationPersonalInformationPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AuthenticationPersonalInformationInteractorProtocol {
        get { return self.baseInteractor as! AuthenticationPersonalInformationInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AuthenticationPersonalInformationRouterProtocol {
        get { return self.baseRouter as! AuthenticationPersonalInformationRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AuthenticationPersonalInformationPresenterDelegate,
         interactor: AuthenticationPersonalInformationInteractorProtocol,
         router: AuthenticationPersonalInformationRouterProtocol,
         registerRequest: RegisterRequest) {
        self.registerRequest = registerRequest
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var registerRequest: RegisterRequest
    
    var statusModel: StatusModel?
    var name: String = ""
    var surname: String = ""
    var email: String = ""
    var password: String = ""
    var isSecureText: Bool = true
}

// MARK: - AuthenticationPersonalInformationPresenterProtocol
extension AuthenticationPersonalInformationPresenter: AuthenticationPersonalInformationPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        setTitleAndDescriptionText()
        view?.didSetAgreementText("Bilgilerinizi göndererek Kullanım Şartlarını ve Gizlilik Sözleşmesini kabul etmiş sayılırsınız.")
        setContinueButtonTitle()
    }
    
    private func setTitleAndDescriptionText() {
        view?.didSetTitleAndDescriptionText(DesignKitL10n.Authentication.PersonalInformation.title,
                                            DesignKitL10n.Authentication.PersonalInformation.description)
    }
    
    private func setContinueButtonTitle() {
        view?.updateContinueButtonTitle(DesignKitL10n.Authentication.PersonalInformation.buttonTitle)
    }
    
    private func navigate(_ routes: AuthenticationPersonalInformationRoutes) {
        router.handleRouter(routes)
    }
    
    func textFieldDidEndEditing(text: String, tag: Int) {
        switch tag {
        case 0:
            name = text
        case 1:
            surname = text
        case 2:
            email = text
        case 3:
            password = text
            let isValid = text.isValidPassword()
            
            statusModel = StatusModel(
                titleText: DesignKitL10n.Authentication.PersonalInformation.statusTitle,
                titleTextColor: DesignKitColorName.contentStrong900.color,
                image: isValid ? Asset.passwordSuccess.name : Asset.passwordError.name,
                imageWidth: 6,
                imageHeight: 12,
                descriptionText: isValid ? DesignKitL10n.Authentication.PersonalInformation.statusCorrect : DesignKitL10n.Authentication.PersonalInformation.statusWrong,
                descriptionTextColor: isValid ? DesignKitColorName.successDark800.color : DesignKitColorName.red500.color,
                descriptionTextFont: .bold04Compact
            )
            view?.reloadData()
        default:
            break
        }

        view?.continueButtonEnabled(isEnabled: isFormValid())
    }
    
    private func isFormValid() -> Bool {
        return !name.isEmpty &&
               !surname.isEmpty &&
               email.isValidEmail() &&
               password.isValidPassword()
    }
    
    func textFieldImageTapped(tag: Int, indexPath: IndexPath?) {
        switch tag {
        case 3:
            guard let indexPath else { return }
            isSecureText = !isSecureText
            view?.reloadData()
            view?.didSetFocusTextField(at: indexPath)
        default:
            break
        }
    }
    
    func didTappedButton(tag: Int) {
        registerRequest.name = name
        registerRequest.surname = surname
        registerRequest.email = email
        registerRequest.password = password
        navigate(.username(registerRequest: registerRequest))
    }
    
    func openTermsOfUse() {
        navigate(.webView(title: "Kullanım Şartları",
                          url: "https://accounts.sporthor.com/Agreement/TermsofUse"))
    }
    
    func openPrivacyPolicy() {
        navigate(.webView(title: "Gizlilik Sözlşemesi",
                          url: "https://accounts.sporthor.com/Agreement/Privacy"))
    }
}

// MARK: - AuthenticationPersonalInformationInteractorDelegate
extension AuthenticationPersonalInformationPresenter: AuthenticationPersonalInformationInteractorDelegate {

}
