//
//  SplashPresenter.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//
//

import Factory
import Foundation
import SignalRServiceKit

final class SplashPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SplashPresenterDelegate? {
        get { return self.baseView as? SplashPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SplashInteractorProtocol {
        get { return self.baseInteractor as! SplashInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SplashRouterProtocol {
        get { return self.baseRouter as! SplashRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SplashPresenterDelegate,
         interactor: SplashInteractorProtocol,
         router: SplashRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - SplashPresenterProtocol
extension SplashPresenter: SplashPresenterProtocol {
    func viewDidLoad() {
        if !ApplicationContext.shared.isFirstLaunch {
            KeychainManager.shared.clearAll()
            ApplicationContext.shared.isFirstLaunch = true
        }
        
        Container.shared.chatSignalRService()?.configure(with: URL(string: "wss://socket.sporthor.com/chathub")!)
        view?.prepareUI()
        Task {
            @MainActor in
            await getLocalizations()
        }   
    }
    
    private func navigate(_ routes: SplashRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func nextGoPage() {
        Task { @MainActor in
            await interactor.getConfiguration()
        }
    }
    
    private func getLocalizations() async {
        Task {
            @MainActor in
            await interactor.getLocalizations()
        }
    }
}

// MARK: - SplashInteractorDelegate
extension SplashPresenter: SplashInteractorDelegate {
    func didGetLocalization() {
        nextGoPage()
    }
    
    func didGetConfiguration(_ response: GetConfigurationResponse) {
        ApplicationContext.shared.getConfiguration = response
        
        if let authToken = ApplicationContext.shared.authToken, !authToken.isEmpty {
            navigate(.tabbar)
        } else {
            navigate(.onboarding)
        }
    }
}
