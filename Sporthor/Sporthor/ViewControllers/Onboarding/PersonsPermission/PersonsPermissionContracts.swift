//
//  PersonsPermissionContracts.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

protocol PersonsPermissionPresenterProtocol: BasePresenterProtocol {
    var view: PersonsPermissionPresenterDelegate? { get set }
    var interactor: PersonsPermissionInteractorProtocol { get set }
    var router: PersonsPermissionRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedButton(_ tag: Int)
}

protocol PersonsPermissionPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func prepareUI()
}

protocol PersonsPermissionInteractorProtocol: BaseInteractorProtocol {
    var delegate: PersonsPermissionInteractorDelegate? { get set }
}

protocol PersonsPermissionInteractorDelegate: BaseInteractorDelegate {
}

protocol PersonsPermissionRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: PersonsPermissionRoutes)
}

enum PersonsPermissionRoutes {
    case authenticationTeam
    case home
}
