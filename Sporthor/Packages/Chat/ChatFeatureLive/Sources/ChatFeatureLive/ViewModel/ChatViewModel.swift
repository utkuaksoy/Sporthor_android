//
//  ChatViewModel.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 29.01.2025.
//

import ChatKit
import DesignKit
import Factory
import Foundation
import MessageKit
import NetworkKit
import SignalRServiceKit
import SignalRClient
import UIKit
import UserNotifications

// MARK: - Implementation

final class ChatViewModelImpl: ChatViewModelProtocol {
    
    // MARK: - Dependencies
    @LazyInjected(\.chatSignalRService) private var chatService
    @LazyInjected(\.networkManager) private var networkManager
    
    // MARK: - Properties
    
    weak var delegate: ChatViewModelDelegate?
    
    private var messages: [ChatMessage] = []
    private let messageProcessor: MessageProcessing
    private var currentPage: Int = 1
    private var isLoadingMessages = false
    private var configureTask: Task<Void, Never>?

    // MARK: - Output Properties
    let currentUser: ChatUser
    let chatPartner: ChatUser
    var users: [UserModel]?

    var messageCount: Int { messages.count }
    var hasMoreMessages = true
    var isNewCreated: Bool = false
    
    // MARK: - Initialization
    
    init(
        chatPartner: ChatUser,
        messageProcessor: MessageProcessing = MessageProcessor(),
        isNewCreated: Bool = false
    ) {
        self.chatPartner = chatPartner
        self.messageProcessor = messageProcessor
        self.currentUser = .init(
            senderId: Container.shared.userManager()?.userId ?? "",
            displayName: Container.shared.userManager()?.userName ?? "",
            image: Container.shared.userManager()?.userAvatar ?? ""
        )
        self.isNewCreated = isNewCreated
        setupSignalR()
    }
    
    func viewDidLoad() {
        configureChat(channelId: chatPartner.senderId)
    }
    
    func viewWillAppear() {
        chatService?.enterChatScreen()
    }
    
    func viewWillDisappear() {
        configureTask?.cancel()
        configureTask = nil
        chatService?.leaveChatScreen()
    }
    
    private func configureChat(channelId: String) {
        configureTask = Task { [weak self] in
            guard let self = self else { return }

            await MainActor.run {
                self.delegate?.showLoading(true)
            }

            async let messagesResult = self.fetchChatMessagesAsync(channelId: channelId)
            async let connectionResult = self.connectAsync(to: channelId)

            let (fetchedMessages, _) = await (messagesResult, connectionResult)

            await MainActor.run {
                self.messages = fetchedMessages
                self.currentPage += 1
                self.delegate?.reloadData()
                self.delegate?.showLoading(false)
            }
        }
    }
    
    private func fetchChatMessagesAsync(channelId: String) async -> [ChatMessage] {
        guard let networkManager else { return [] }
        let request = ChatServiceNetworkTask.getMessages(channelId: channelId, page: self.currentPage)
        let result = await networkManager.request(service: request, responseType: ChatResponse.self, showLoading: false)
        
        switch result {
        case .success(let response):
            guard let messages = response.messages else { return [] }
            self.users = response.users
            return messages.map { message in
                ChatMessage(
                    messageId: message.id ?? UUID().uuidString,
                    sender: ChatUser(
                        senderId: message.from?.id ?? "",
                        displayName: message.from?.name ?? "",
                        image: message.from?.image ?? ""
                    ),
                    sentDate: message.sendDate?.toDate() ?? Date(),
                    content: message.content ?? "",
                    messageType: ChatMessageType(rawValue: message.messageType ?? 0) ?? .text,
                    attachment: message.fileExtension
                )
            }
        case .failure(let error):
            print(error.localizedDescription)
            return []
        }
    }
    
    private func connectAsync(to group: String) async -> Bool {
        guard let chatService = chatService else { return false }
        chatService.connect(userId: currentUser.senderId, groupId: group)
        for _ in 0..<10 {
            try? await Task.sleep(nanoseconds: 500_000_000)
            if chatService.connectionState == .connected {
                return true
            }
        }
        return false
    }
    
    // MARK: - Output Methods

    func message(at index: Int) -> ChatMessage {
        messages[index]
    }
    
    // MARK: - ChatViewModelInput
    
    private func addMessage(_ message: ChatMessage) {
        messages.append(message)
        delegate?.didReceiveMessage()
    }
    
    func sendTextMessage(_ text: String) {
        guard !text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        
        let message = ChatMessage(
            messageId: UUID().uuidString,
            sender: currentUser,
            sentDate: Date(),
            content: text,
            messageType: .text
        )
        
        addMessage(message)
        sendToServer(message: text, type: .text)
    }
    
    func sendImage(_ image: UIImage, imageType: String) {
        messageProcessor.processImage(image) { [weak self] result in
            guard let self = self else { return }
            
            switch result {
            case .success(let base64String):
                let message = self.createMessage(content: base64String, type: .image, fileExtension: imageType)
                DispatchQueue.main.async {
                    self.addMessage(message)
                    self.sendToServer(message: base64String, type: .image, fileType: imageType)
                }
            case .failure(_):
                self.handleError(ChatError.imageProcessingFailed)
            }
        }
    }
    
    func sendVideo(url: URL, videoType: String) {
        messageProcessor.processVideo(url) { [weak self] result in
            guard let self = self else { return }
            
            switch result {
            case .success(let base64String):
                let message = self.createMessage(content: base64String, type: .video, fileExtension: videoType)
                self.addMessage(message)
                self.sendToServer(message: base64String, type: .video, fileType: videoType)
            case .failure(_):
                self.handleError(ChatError.videoProcessingFailed)
            }
        }
    }
    
    func sendFile(fileUrl: URL) {
        messageProcessor.processFile(fileUrl) { [weak self] result in
            guard let self = self else { return }
            
            switch result {
            case .success((let base64String)):
                let fileType = fileUrl.pathExtension.lowercased()
                let message = self.createMessage(content: base64String, type: .file, fileExtension: fileType)
                DispatchQueue.main.async {
                    self.addMessage(message)
                    self.sendToServer(
                        message: base64String,
                        type: .file,
                        fileType: fileType
                    )
                }
            case .failure(_):
                self.handleError(ChatError.fileProcessingFailed)
            }
        }
    }
    
    func notifyTyping() {
//        chatService?.notifyTyping()
    }
    
    // MARK: - Private Methods

    private func setupSignalR() {
        chatService?.delegate = self
    }
    
    private func createMessage(content: String, type: ChatMessageType, fileExtension: String? = nil) -> ChatMessage {
        ChatMessage(
            messageId: UUID().uuidString,
            sender: currentUser,
            sentDate: Date(),
            content: content,
            messageType: type,
            attachment: fileExtension
        )
    }
    
    private func sendToServer(message: String, type: ChatMessageType, fileType: String? = nil) {
        chatService?.sendMessage(
            type: type.socketMessageType,
            message: message,
            fileType: fileType
        )
    }
    
    private func handleError(_ error: Error) {
        DispatchQueue.main.async { [weak self] in
            let localizedError: String
            
            if let signalRError = error as? SignalRClient.SignalRError {
                switch signalRError {
                case .invalidState:
                    localizedError = L10n.Chat.Error.serviceInitFailed
                case .serverClose:
                    localizedError = L10n.Chat.Error.connectionFailed
                case .connectionIsReconnecting:
                    localizedError = L10n.Chat.Error.reconnecting
                case .hubInvocationError:
                    localizedError = L10n.Chat.Error.messageSendFailed
                default:
                    localizedError = L10n.Chat.Error.connectionFailed
                }
            } else {
                localizedError = error.localizedDescription
            }
            self?.delegate?.didEncounterError(ChatError.networkError(localizedError))
        }
    }
    
    func loadPreviousMessages(completion: @escaping (Bool) -> Void) {
        guard !isLoadingMessages else {
            completion(false)
            return
        }
        
        guard hasMoreMessages else {
            completion(false)
            return
        }
        
        isLoadingMessages = true
        
        Task { [weak self] in
            guard let self = self else {
                completion(false)
                return
            }
            
            let previousMessages = await fetchChatMessagesAsync(channelId: chatPartner.senderId)
            
            await MainActor.run {
                self.isLoadingMessages = false
                
                if previousMessages.isEmpty {
                    self.hasMoreMessages = false
                    completion(false)
                    return
                }
                
                self.messages.insert(contentsOf: previousMessages, at: 0)
                self.currentPage += 1
                
                let expectedPageSize = 20
                self.hasMoreMessages = previousMessages.count >= expectedPageSize
                
                completion(true)
            }
        }
    }
}

// MARK: - SignalRServiceDelegate

extension ChatViewModelImpl: SignalRServiceDelegate {
    
    func didReceiveMessage(
        from userId: String,
        displayName: String,
        message: String,
        type: Int,
        fileType: String?
    ) {
        guard userId != currentUser.senderId else { return }
        
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            
            let messageType = ChatMessageType(rawValue: type) ?? .text
            let sender = ChatUser(senderId: userId, displayName: displayName, image: "")
            let chatMessage = ChatMessage(
                messageId: UUID().uuidString,
                sender: sender,
                sentDate: Date(),
                content: message,
                messageType: messageType,
                attachment: fileType
            )
            self.addMessage(chatMessage)
        }
    }
    
    func userTyping(username: String) {
        DispatchQueue.main.async { [weak self] in
            self?.delegate?.didUpdateTypingStatus(isTyping: true, username: username)
        }
    }
    
    func didConnect() {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.delegate?.didUpdateConnectionStatus(isConnected: true)
        }
    }
    
    func didDisconnect(error: Error?) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.delegate?.didUpdateConnectionStatus(isConnected: false)
        }
    }
    
    func didEncounterError(_ error: Error) {
        handleError(error)
    }
}

