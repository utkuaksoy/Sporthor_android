//
//  OnboardingPresenter.swift
//  Sporthor
//
//  Created by derTurke on 6.02.2025.
//
//

import Foundation

final class OnboardingPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: OnboardingPresenterDelegate? {
        get { return self.baseView as? OnboardingPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: OnboardingInteractorProtocol {
        get { return self.baseInteractor as! OnboardingInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: OnboardingRouterProtocol {
        get { return self.baseRouter as! OnboardingRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: OnboardingPresenterDelegate,
         interactor: OnboardingInteractorProtocol,
         router: OnboardingRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - OnboardingPresenterProtocol
extension OnboardingPresenter: OnboardingPresenterProtocol {
    func viewDidLoad() {
        view?.didSetBackgroundImage(image: Asset.wellcome.name)
        view?.prepareUI()
    }
    
    func viewWillAppear() {
        view?.updateNavigationBarHidden(true)
    }
    
    func viewWillDisappear() {
        view?.updateNavigationBarHidden(false)
    }
    
    private func navigate(_ routes: OnboardingRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedButton(tag: Int) {
        switch tag {
        case 0:
            navigate(.register)
        case 1:
            navigate(.login)
        case 2:
            navigate(.discover)
        default:
            break
        }
    }
}

// MARK: - OnboardingInteractorDelegate
extension OnboardingPresenter: OnboardingInteractorDelegate {

}
