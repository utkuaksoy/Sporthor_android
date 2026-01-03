//
//  AccountSettingsContracts.swift
//  Sporthor
//
//  Created by derTurke on 31.07.2025.
//
//

import Foundation

protocol AccountSettingsPresenterProtocol: BasePresenterProtocol {
    var view: AccountSettingsPresenterDelegate? { get set }
    var interactor: AccountSettingsInteractorProtocol { get set }
    var router: AccountSettingsRouterProtocol { get set }
    var isPrivateAccount: Bool { get set }
    var isDeleteAccount: Bool { get set }
    
    func viewDidLoad()
    func didChangeSwitch(isOn: Bool, tag: Int)
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectRowAt(_ index: Int)
}

protocol AccountSettingsPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol AccountSettingsInteractorProtocol: BaseInteractorProtocol {
    var delegate: AccountSettingsInteractorDelegate? { get set }
    func deleteAccount() async
    func updateProfilePublicPrivate(_ request: [String: Any]) async
}

protocol AccountSettingsInteractorDelegate: BaseInteractorDelegate {
    func didUpdateProfilePublicPrivate()
}

protocol AccountSettingsRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AccountSettingsRoutes)
}

enum AccountSettingsRoutes {
    case back
    case blockUser
}
