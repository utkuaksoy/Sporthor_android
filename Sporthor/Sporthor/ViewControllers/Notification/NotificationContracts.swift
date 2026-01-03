//
//  NotificationContracts.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//
//

import Foundation

protocol NotificationPresenterProtocol: BasePresenterProtocol {
    var view: NotificationPresenterDelegate? { get set }
    var interactor: NotificationInteractorProtocol { get set }
    var router: NotificationRouterProtocol { get set }
    var headerTabs: [NotificationHeaderModel] { get set }
    var filteredNotifications: [NotificationModel] { get }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectHeaderTab(model: NotificationHeaderModel)
    func openProfile(model: NotificationModel)
    func openChat(model: NotificationModel)
    func didTappedTrainingGroupRequestButton(_ model: NotificationModel, isAccepted: Bool)
    func didTappedFollowRequestButton(_ model: NotificationModel, isAccepted: Bool) 
}

protocol NotificationPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol NotificationInteractorProtocol: BaseInteractorProtocol {
    var delegate: NotificationInteractorDelegate? { get set }
    func getNotifications() async
    func confirmationTrainingGroupUser(_ request: [String: Any]) async
    func confirmationFollow(_ request: [String: Any]) async
}

protocol NotificationInteractorDelegate: BaseInteractorDelegate {
    func didGetNotifications(_ notifications: [NotificationModel])
    func didConfirmationTrainingGroupUser()
}

protocol NotificationRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: NotificationRoutes)
}

enum NotificationRoutes {
    case back
    case openProfile(userId: String, username: String)
    case chat(
        userId: String?,
        displayName: String?,
        image: String?,
        isGroup: Bool,
        toUserId: String?
    )
}
