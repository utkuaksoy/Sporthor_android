//
//  NextOnboardingPresenter.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation
import CommonKit

final class NextOnboardingPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: NextOnboardingPresenterDelegate? {
        get { return self.baseView as? NextOnboardingPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: NextOnboardingInteractorProtocol {
        get { return self.baseInteractor as! NextOnboardingInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: NextOnboardingRouterProtocol {
        get { return self.baseRouter as! NextOnboardingRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: NextOnboardingPresenterDelegate,
         interactor: NextOnboardingInteractorProtocol,
         router: NextOnboardingRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    var onboarding: [OnboardingModel] = []
    private var currentIndex: Int = 0
}

// MARK: - NextOnboardingPresenterProtocol
extension NextOnboardingPresenter: NextOnboardingPresenterProtocol {
    func viewDidLoad() {
        view?.setupView()
        prepareOnboarding()
        
    }
    
    private func navigate(_ routes: NextOnboardingRoutes) {
        router.handleRouter(routes)
    }
    
    private func prepareOnboarding() {
        Task {
            @MainActor in
            await interactor.getOnboarding()
        }
    }
    
    func changePage(to index: Int, isButtonClicked: Bool = false) {
        currentIndex = isButtonClicked ? index + 1 : index
        if currentIndex < onboarding.count {
            if isButtonClicked {
                view?.scrollToItem(nextIndex: currentIndex)
            }
            view?.changeButtonTitle(currentIndex == onboarding.count - 1 ? DesignKitL10n.NextOnboarding.lastElementButtonTitle : DesignKitL10n.NextOnboarding.continueButtonTitle)
            view?.didSetCurrentPageControl(currentIndex)
        } else {
            navigate(.personsPermission)
        }
    }
    
    func closeButtonTapped() {
        if ApplicationContext.shared.isSelectedCoach || ApplicationContext.shared.isSelectedClubOfficial {
            navigate(.authenticationTeam)
        } else {
            navigate(.home)
        }
    }
    
    private func permissionNotification() {
        
    }
}

// MARK: - NextOnboardingInteractorDelegate
extension NextOnboardingPresenter: NextOnboardingInteractorDelegate {
    func didGetOnboarding(_ response: OnboardingResponse) {
        onboarding = response.pages ?? []
        view?.didSetNumberOfPages(onboarding.count)
        view?.reloadData()
    }
}
