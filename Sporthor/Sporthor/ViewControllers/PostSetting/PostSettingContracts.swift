//
//  PostSettingContracts.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

protocol PostSettingPresenterProtocol: BasePresenterProtocol {
    var view: PostSettingPresenterDelegate? { get set }
    var interactor: PostSettingInteractorProtocol { get set }
    var router: PostSettingRouterProtocol { get set }
    var items: [PostSettingItems] { get set }
    
    func viewDidLoad()
    func didSelectRow(at indexPath: IndexPath)
}

protocol PostSettingPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol PostSettingInteractorProtocol: BaseInteractorProtocol {
    var delegate: PostSettingInteractorDelegate? { get set }
    func hidePost(_ request: [String: Any]) async
    func deletePost(_ request: [String: Any]) async
    func blockUser(_ request: [String: Any]) async
}

protocol PostSettingInteractorDelegate: BaseInteractorDelegate {
    func didHideOrDeletePost()
    func didBlockUser()
}

protocol PostSettingRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: PostSettingRoutes)
}

enum PostSettingRoutes {
    case complain(delegate: ComplainDelegate?, post: Post)
    case dismissHideOrRemove(delegate: PostSettingDelegate?, post: Post)
    case blockUser(delegate: PostSettingDelegate?, post: Post)
}

protocol PostSettingDelegate: AnyObject {
    func dismissHideOrRemove(model: Post)
    func didBlockUser(model: Post)
}

extension PostSettingDelegate {
    func dismissHideOrRemove(model: Post) {}
    func didBlockUser(model: Post) {}
}
