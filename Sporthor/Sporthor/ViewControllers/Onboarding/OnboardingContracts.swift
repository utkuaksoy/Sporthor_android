//
//  OnboardingContracts.swift
//  Sporthor
//
//  Created by derTurke on 6.02.2025.
//
//

import Foundation

protocol OnboardingPresenterProtocol: BasePresenterProtocol {
    var view: OnboardingPresenterDelegate? { get set }
    var interactor: OnboardingInteractorProtocol { get set }
    var router: OnboardingRouterProtocol { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func viewWillDisappear()
    func didTappedButton(tag: Int)
}

protocol OnboardingPresenterDelegate: BasePresenterDelegate {
    func didSetBackgroundImage(image: String)
    func updateNavigationBarHidden(_ isHidden: Bool)
    func prepareUI()
}

protocol OnboardingInteractorProtocol: BaseInteractorProtocol {
    var delegate: OnboardingInteractorDelegate? { get set }
}

protocol OnboardingInteractorDelegate: BaseInteractorDelegate {
}

protocol OnboardingRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: OnboardingRoutes)
}

enum OnboardingRoutes {
    case register
    case login
    case discover
}
