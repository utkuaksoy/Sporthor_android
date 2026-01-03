//
//  NextOnboardingContracts.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

protocol NextOnboardingPresenterProtocol: BasePresenterProtocol {
    var view: NextOnboardingPresenterDelegate? { get set }
    var interactor: NextOnboardingInteractorProtocol { get set }
    var router: NextOnboardingRouterProtocol { get set }
    var onboarding: [OnboardingModel] { get set }
    
    func viewDidLoad()
    func changePage(to index: Int, isButtonClicked: Bool)
    func closeButtonTapped()
}

protocol NextOnboardingPresenterDelegate: BasePresenterDelegate {
    func setupView()
    func didSetNumberOfPages(_ pages: Int)
    func reloadData()
    func scrollToItem(nextIndex: Int)
    func changeButtonTitle(_ title: String)
    func didSetCurrentPageControl(_ page: Int)
}

protocol NextOnboardingInteractorProtocol: BaseInteractorProtocol {
    var delegate: NextOnboardingInteractorDelegate? { get set }
    func getOnboarding() async
}

protocol NextOnboardingInteractorDelegate: BaseInteractorDelegate {
    func didGetOnboarding(_ response: OnboardingResponse)
}

protocol NextOnboardingRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: NextOnboardingRoutes)
}

enum NextOnboardingRoutes {
    case authenticationTeam
    case home
    case personsPermission
}
