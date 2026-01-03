//
//  SettingsPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation

final class SettingsPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SettingsPresenterDelegate? {
        get { return self.baseView as? SettingsPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SettingsInteractorProtocol {
        get { return self.baseInteractor as! SettingsInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SettingsRouterProtocol {
        get { return self.baseRouter as! SettingsRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SettingsPresenterDelegate,
         interactor: SettingsInteractorProtocol,
         router: SettingsRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - SettingsPresenterProtocol
extension SettingsPresenter: SettingsPresenterProtocol {
    func viewDidLoad() {
        // Initial setup if needed
    }
    
    func handleRoute(_ route: SettingsRoutes) {
        switch route {
        case .logout:
            interactor.logout()
        default:
            router.handleRouter(route)
        }
    }
}

// MARK: - SettingsInteractorDelegate
extension SettingsPresenter: SettingsInteractorDelegate {
    func logoutSuccess() {
        router.handleRouter(.logout)
    }
    
    func logoutFailed(_ error: Error) {
        // Handle logout error
    }
}
