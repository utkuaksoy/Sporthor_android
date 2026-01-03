//
//  ExperienceMainPresenter.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceMainPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ExperienceMainPresenterDelegate? {
        get { return self.baseView as? ExperienceMainPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ExperienceMainInteractorProtocol {
        get { return self.baseInteractor as! ExperienceMainInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ExperienceMainRouterProtocol {
        get { return self.baseRouter as! ExperienceMainRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ExperienceMainPresenterDelegate,
         interactor: ExperienceMainInteractorProtocol,
         router: ExperienceMainRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - ExperienceMainPresenterProtocol
extension ExperienceMainPresenter: ExperienceMainPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetMainTitle(DesignKitL10n.Experience.Main.title)
        view?.didSetContinueButttonTitle(DesignKitL10n.Experience.Main.buttonTitle)
    }
    
    private func navigate(_ routes: ExperienceMainRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedButton(with tag: Int) {
        navigate(.experienceJob)
    }
}

// MARK: - ExperienceMainInteractorDelegate
extension ExperienceMainPresenter: ExperienceMainInteractorDelegate {

}
