//
//  ProfileSettingContracts.swift
//  Sporthor
//
//  Created by derTurke on 16.08.2025.
//
//

import Foundation

protocol ProfileSettingPresenterProtocol: BasePresenterProtocol {
    var view: ProfileSettingPresenterDelegate? { get set }
    var interactor: ProfileSettingInteractorProtocol { get set }
    var router: ProfileSettingRouterProtocol { get set }
    var items: [ProfileSettingItems] { get set }
    
    func viewDidLoad()
    func didSelectRow(at indexPath: IndexPath)
}

protocol ProfileSettingPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol ProfileSettingInteractorProtocol: BaseInteractorProtocol {
    var delegate: ProfileSettingInteractorDelegate? { get set }
    func blockUser(_ request: [String: Any]) async
}

protocol ProfileSettingInteractorDelegate: BaseInteractorDelegate {
    func didBlockUser()
}

protocol ProfileSettingRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ProfileSettingRoutes)
}

enum ProfileSettingRoutes {
    case blockUser(delegate: ProfileSettingDelegate?)
}

protocol ProfileSettingDelegate: AnyObject {
    func didBlockUser()
}
