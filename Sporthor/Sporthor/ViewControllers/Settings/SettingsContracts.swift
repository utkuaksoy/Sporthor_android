//
//  SettingsContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation

protocol SettingsPresenterProtocol: BasePresenterProtocol {
    var view: SettingsPresenterDelegate? { get set }
    var interactor: SettingsInteractorProtocol { get set }
    var router: SettingsRouterProtocol { get set }
    
    func viewDidLoad()
    func handleRoute(_ route: SettingsRoutes)
}

protocol SettingsPresenterDelegate: BasePresenterDelegate {
}

protocol SettingsInteractorProtocol: BaseInteractorProtocol {
    var delegate: SettingsInteractorDelegate? { get set }
    
    func logout()
}

protocol SettingsInteractorDelegate: BaseInteractorDelegate {
    func logoutSuccess()
    func logoutFailed(_ error: Error)
}

protocol SettingsRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SettingsRoutes)
}

enum SettingsRoutes {
    case accountSettings
    case about
    case termsOfUse
    case privacyPolicy
    case contactCenter
    case logout
}
