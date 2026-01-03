//
//  AccountBlokeListContracts.swift
//  Sporthor
//
//  Created by derTurke on 15.08.2025.
//
//

import Foundation

protocol AccountBlockUserListPresenterProtocol: BasePresenterProtocol {
    var view: AccountBlockUserListPresenterDelegate? { get set }
    var interactor: AccountBlockUserListInteractorProtocol { get set }
    var router: AccountBlockUserListRouterProtocol { get set }
    var users: [GetTrainingGroupUserModelUser] { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectRowAt(_ indexPath: IndexPath)
}

protocol AccountBlockUserListPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol AccountBlockUserListInteractorProtocol: BaseInteractorProtocol {
    var delegate: AccountBlockUserListInteractorDelegate? { get set }
    
    func getBlockUser() async
    func blockUser(_ request: [String: Any], isAddBlock: Bool) async
}

protocol AccountBlockUserListInteractorDelegate: BaseInteractorDelegate {
    func didGetBlockUser(_ users: [GetTrainingGroupUserModelUser])
    func didBlockUser(isAddBlock: Bool)
}

protocol AccountBlockUserListRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: AccountBlockUserListRoutes)
}

enum AccountBlockUserListRoutes {
    case back
}
