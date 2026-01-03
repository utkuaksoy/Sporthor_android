//
//  AccountSettingsPresenter.swift
//  Sporthor
//
//  Created by derTurke on 31.07.2025.
//
//

import Foundation
import ComponentKit

final class AccountSettingsPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AccountSettingsPresenterDelegate? {
        get { return self.baseView as? AccountSettingsPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AccountSettingsInteractorProtocol {
        get { return self.baseInteractor as! AccountSettingsInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AccountSettingsRouterProtocol {
        get { return self.baseRouter as! AccountSettingsRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AccountSettingsPresenterDelegate,
         interactor: AccountSettingsInteractorProtocol,
         router: AccountSettingsRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var isPrivateAccount: Bool = ApplicationContext.shared.isPrivateAccount
    var isDeleteAccount: Bool = false
}

// MARK: - AccountSettingsPresenterProtocol
extension AccountSettingsPresenter: AccountSettingsPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Hesap Ayarları")
        view?.prepareUI()
    }
    
    private func navigate(_ routes: AccountSettingsRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didChangeSwitch(isOn: Bool, tag: Int) {
        switch tag {
        case 0:
            isPrivateAccount = isOn
            ApplicationContext.shared.isPrivateAccount = isOn
            updateProfilePublicPrivate()
        case 1:
            isDeleteAccount = isOn
            if isOn {
                showCKDefaultAlert(delegate: self,
                                   message: "Hesabınızı silmek istediğinizden emin misiniz?",
                                   okTitle: "Evet",
                                   cancelTitle: "Hayır")
            }
        default:
            break
        }
    }
    
    private func deleteAccount() {
        Task { @MainActor in
            await interactor.deleteAccount()
        }
    }
    
    private func updateProfilePublicPrivate() {
        let request: [String: Any] = ["isPublic": !isPrivateAccount]
        Task { @MainActor in
            await interactor.updateProfilePublicPrivate(request)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
    
    func didSelectRowAt(_ index: Int) {
        switch index {
        case 2:
            navigate(.blockUser)
        default:
            break
        }
    }
}

// MARK: - AccountSettingsInteractorDelegate
extension AccountSettingsPresenter: AccountSettingsInteractorDelegate {
    func didUpdateProfilePublicPrivate() {
        view?.reloadData()
    }
}

extension AccountSettingsPresenter: CKDefaultAlertDelegate {
    func ckDefaultAlertDidTapOK() {
        deleteAccount()
    }
    
    func ckDefaultAlertDidTapCancel() {
        isDeleteAccount = false
        view?.reloadData()
    }
}
