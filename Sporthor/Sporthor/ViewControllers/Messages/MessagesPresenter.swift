//
//  MessagesPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.03.2025.
//
//

import Factory
import Foundation
import Combine

final class MessagesPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: MessagesPresenterDelegate? {
        get { return self.baseView as? MessagesPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: MessagesInteractorProtocol {
        get { return self.baseInteractor as! MessagesInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: MessagesRouterProtocol {
        get { return self.baseRouter as! MessagesRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Private Properties
    private var isFirstLoad: Bool = true
    private var messageList: [MessagesItemModel]?
    private var filteredMessageList: [MessagesItemModel]?
    private var cancellables = Set<AnyCancellable>()
    @LazyInjected(\.pushNotificationService) private var pushNotificationService
    
    // MARK: - Public Properties

    var messages: [MessagesItemModel]? {
        filteredMessageList ?? messageList
    }
    
    var isSearchActive: Bool {
        filteredMessageList != nil
    }
    
    var hasNoSearchResults: Bool {
        filteredMessageList?.isEmpty == true
    }
    
    // MARK: - Initialize

    init(view: MessagesPresenterDelegate,
         interactor: MessagesInteractorProtocol,
         router: MessagesRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        setupPushNotificationSubscription()
    }
    
    deinit {
        cancellables.removeAll()
    }
    
    private func setupPushNotificationSubscription() {
        pushNotificationService.messagePublisher
            .receive(on: DispatchQueue.main)
            .sink { [weak self] messageData in
                self?.updateMessageWithPushData(messageData: messageData)
            }
            .store(in: &cancellables)
    }
}

// MARK: - MessagesPresenterProtocol

extension MessagesPresenter: MessagesPresenterProtocol {
    func viewDidLoad() {
        view?.configureHeaderView()
        if isFirstLoad {
            fetchMessages()
            isFirstLoad = false
        }
        
    }
    
    func viewWillAppear() {
        if !isFirstLoad {
            fetchMessages()
        }
    }
    
    func getItem(with indexPath: IndexPath) -> MessagesItemModel? {
        messages?[safe: indexPath.row]
    }
    
    func didSelectMessage(
        userId: String?,
        displayName: String?,
        image: String?,
        isGroup: Bool,
        toUserId: String?
    ) {
        navigate(
            .chat(
                userId: userId,
                displayName: displayName,
                image: image,
                isGroup: isGroup,
                isNewCreated: false,
                toUserId: toUserId
            )
        )
    }
    
    func markMessageAsRead(for indexPath: IndexPath) {
        updateUnreadCount(for: indexPath)
        sendMarkAsReadRequest(for: indexPath)
        view?.reloadRow(for: indexPath)
    }
    
    func didTapDelete(item: MessagesItemModel, indexPath: IndexPath) {
        deleteMessage(at: indexPath)
    }
    
    func didTapMute(item: MessagesItemModel, indexPath: IndexPath) {
        // TODO: Implement mute functionality
    }
    
    func searchMessages(with text: String) {
        if text.isEmpty {
            clearSearch()
        } else {
            performSearch(with: text)
        }
        view?.reloadData()
    }
    
    func updateMessageWithPushData(messageData: PushMessageData) {
        if let index = messageList?.firstIndex(where: { $0.userId == messageData.groupId }) {
            messageList?[index].unReadMessageCount = Int(messageData.unReadCount) ?? .zero
            messageList?[index].lastMessage = messageData.lastMessage
            
            if isSearchActive,
               let filteredIndex = filteredMessageList?.firstIndex(where: { $0.messageId == messageData.messageId }) {
                filteredMessageList?[filteredIndex].unReadMessageCount = Int(messageData.unReadCount) ?? .zero
                filteredMessageList?[filteredIndex].lastMessage = messageData.lastMessage
                
                DispatchQueue.main.async { [weak self] in
                    self?.view?.reloadRow(for: IndexPath(row: filteredIndex, section: 0))
                }
            } else {
                DispatchQueue.main.async { [weak self] in
                    self?.view?.reloadRow(for: IndexPath(row: index, section: 0))
                }
            }
        } else {
            messageList?.insert(
                .init(
                    messageId: messageData.messageId,
                    isGroup: Bool(messageData.isGroup) ?? false,
                    image: messageData.imageURL,
                    name: messageData.senderName,
                    userName: messageData.senderName,
                    lastMessage: messageData.lastMessage,
                    userId: messageData.groupId,
                    unReadMessageCount: Int(messageData.unReadCount) ?? .zero,
                    messageDate: messageData.sendDate
                ),
                at: .zero
            )
            DispatchQueue.main.async { [weak self] in
                self?.view?.reloadData()
            }
        }
    }
}

// MARK: - Private Methods

private extension MessagesPresenter {
    func fetchMessages() {
        Task {
            await interactor.fetchMessagesService()
        }
    }
    
    func updateUnreadCount(for indexPath: IndexPath) {
        if isSearchActive, let messageId = filteredMessageList?[indexPath.row].messageId {
            filteredMessageList?[indexPath.row].unReadMessageCount = .zero
            if let originalIndex = messageList?.firstIndex(where: { $0.messageId == messageId }) {
                messageList?[originalIndex].unReadMessageCount = .zero
            }
        } else {
            messageList?[indexPath.row].unReadMessageCount = .zero
        }
    }
    
    func sendMarkAsReadRequest(for indexPath: IndexPath) {
        Task {
            await interactor.markMessageAsRead(for: indexPath)
        }
    }
    
    func deleteMessage(at indexPath: IndexPath) {
        let userList = isSearchActive ? filteredMessageList : messageList
        guard let userId = userList?[safe: indexPath.row]?.userId else { return }
        
        Task {
            await interactor.hideMessagesService(at: indexPath, userId: userId)
        }
    }
    
    func clearSearch() {
        filteredMessageList = nil
    }
    
    func performSearch(with text: String) {
        let filtered = messageList?.filter { message in
            message.name.localizedCaseInsensitiveContains(text) ||
            message.lastMessage.localizedCaseInsensitiveContains(text)
        }
        filteredMessageList = filtered ?? []
    }
    
    func navigate(_ routes: MessagesRoutes) {
        router.handleRouter(routes)
    }
}

// MARK: - MessagesInteractorDelegate

extension MessagesPresenter: MessagesInteractorDelegate {
    func fetchMessagesSuccess(messages: [MessagesItemModel]) {
        self.messageList = messages
        DispatchQueue.main.async {
            self.view?.reloadData()
        }
    }
    
    func fetchMessagesError(error: String?) {
        showAlert(type: .error, message: error ?? "")
    }
    
    func hideMessagesServiceSuccess(at indexPath: IndexPath) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            if self.isSearchActive, let userId = self.filteredMessageList?[indexPath.row].userId {
                self.filteredMessageList?.remove(at: indexPath.row)
                if let originalIndex = self.messageList?.firstIndex(where: { $0.userId == userId }) {
                    self.messageList?.remove(at: originalIndex)
                }
            } else {
                self.messageList?.remove(at: indexPath.row)
            }
            self.view?.removeRow(for: indexPath)
        }
    }
}

// MARK: - MessagesHeaderViewDelegate

extension MessagesPresenter: MessagesHeaderViewDelegate {
    func didTapAddButton() {
        DispatchQueue.main.async {
            self.navigate(.createChat)
        }
    }
    
    func didSearchTextChange(_ text: String) {
        searchMessages(with: text)
    }
}
