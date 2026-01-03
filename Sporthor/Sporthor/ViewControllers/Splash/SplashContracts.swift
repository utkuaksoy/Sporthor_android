//
//  SplashContracts.swift
//  Sporthor
//
//  Created by derTurke on 30.01.2025.
//
//

import Foundation

protocol SplashPresenterProtocol: BasePresenterProtocol {
    var view: SplashPresenterDelegate? { get set }
    var interactor: SplashInteractorProtocol { get set }
    var router: SplashRouterProtocol { get set }
    
    func viewDidLoad()
}

protocol SplashPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
}

protocol SplashInteractorProtocol: BaseInteractorProtocol {
    var delegate: SplashInteractorDelegate? { get set }
    
    func getLocalizations() async
    func getConfiguration() async
}

protocol SplashInteractorDelegate: BaseInteractorDelegate {
    func didGetLocalization()
    func didGetConfiguration(_ response: GetConfigurationResponse)
}

protocol SplashRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SplashRoutes)
}

enum SplashRoutes {
    case onboarding
    case tabbar
}
