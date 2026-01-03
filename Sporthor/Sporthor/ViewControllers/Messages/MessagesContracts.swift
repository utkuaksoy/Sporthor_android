//
//  MessagesContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.03.2025.
//
//

import Foundation

protocol MessagesPresenterProtocol: BasePresenterProtocol, MessagesHeaderViewDelegate {
    var view: MessagesPresenterDelegate? { get set }
    var interactor: MessagesInteractorProtocol { get set }
    var router: MessagesRouterProtocol { get set }
    
    var messages: [MessagesItemModel]? { get }
    var isSearchActive: Bool { get }
    var hasNoSearchResults: Bool { get }
    
    func getItem(with indexPath: IndexPath) -> MessagesItemModel?
    func viewDidLoad()
    func viewWillAppear()
    func didSelectMessage(userId: String?, displayName: String?, image: String?, isGroup: Bool, toUserId: String?)
    func didTapDelete(item: MessagesItemModel, indexPath: IndexPath)
    func didTapMute(item: MessagesItemModel, indexPath: IndexPath)
    func markMessageAsRead(for indexPath: IndexPath)
    func updateMessageWithPushData(messageData: PushMessageData)
}

protocol MessagesPresenterDelegate: BasePresenterDelegate {
    func configureHeaderView()
    func reloadData()
    func removeRow(for indexPath: IndexPath)
    func reloadRow(for indexPath: IndexPath)
}

protocol MessagesInteractorProtocol: BaseInteractorProtocol {
    var delegate: MessagesInteractorDelegate? { get set }
    
    func fetchMessagesService() async
    func markMessageAsRead(for indexPath: IndexPath) async
    func hideMessagesService(at indexPath: IndexPath, userId: String) async
}

protocol MessagesInteractorDelegate: BaseInteractorDelegate {
    func fetchMessagesSuccess(messages: [MessagesItemModel])
    func fetchMessagesError(error: String?)
    func hideMessagesServiceSuccess(at indexPath: IndexPath)
}

protocol MessagesRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: MessagesRoutes)
}

enum MessagesRoutes {
    case createChat
    case chat(
        userId: String?,
        displayName: String?,
        image: String?,
        isGroup: Bool,
        isNewCreated: Bool,
        toUserId: String?
    )
}

extension Notification.Name {
    static let messagePushNotificationReceived = Notification.Name("messagePushNotificationReceived")
}
