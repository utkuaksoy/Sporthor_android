//
//  SignalRServiceLive.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 29.01.2025.
//

import Foundation
import SignalRClient
import SignalRServiceKit
import UIKit

public final class SignalRServiceLive: SignalRServiceProtocol {
    
    private enum Constants {
        static let receiveMessage = "ReceiveMessage"
        static let userTyping = "UserTyping"
        static let joinGroup = "JoinGroup"
        static let leaveGroup = "LeaveGroup"
        static let sendMessageToGroup = "SendMessageToGroup"
        static let notifyTyping = "NotifyTyping"
        static let reconnectDelay: TimeInterval = 3
        static let maxReconnectAttempts = 5
        
        enum RetryConfig {
            static let initialDelay: TimeInterval = 2
            static let maxDelay: TimeInterval = 10
            static let multiplier: Double = 2
            static let maxRetryCount = 3
        }
    }

    // MARK: - Private Properties
    
    private var connection: HubConnection?
    private var url: URL?
    private var currentGroup: String?
    private var userId: String?
    private var reconnectAttempts = 0
    private var pendingMessages: [RetryableMessage] = []
    private var disconnectReason: DisconnectionReason = .none
    private var isReconnecting = false
    private var isManualDisconnect: Bool = false
    private var backgroundTask: UIBackgroundTaskIdentifier = .invalid
    private var disconnectionReason: DisconnectionReason = .none
    private var isInBackground: Bool = false

    private let connectionQueue: DispatchQueue = {
        let queue = DispatchQueue(
            label: "com.sporthor.signalr.connection",
            qos: .userInitiated
        )
        return queue
    }()

    // MARK: - Public Properties

    public var isInChatScreen: Bool = false
    public weak var delegate: SignalRServiceDelegate?
    public var isConnected: Bool {
        return connection?.connectionId != nil && connectionState == .connected
    }
    public var connectionState: ConnectionState = .disconnected
    
    public init() {
        setupBackgroundHandling()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        endBackgroundTask()
        disconnect()
    }

    // MARK: - Configure & Setup

    public func configure(with url: URL) {
        self.url = url
        let builder = HubConnectionBuilder(url: url)
            .withHttpConnectionOptions { httpConnectionOptions in
                httpConnectionOptions.skipNegotiation = true
                httpConnectionOptions.requestTimeout = 120000
            }
            .withHubConnectionOptions { options in
                options.keepAliveInterval = 15
            }
            .withLogging(minLogLevel: .info)
        connection = builder.build()
        connection?.delegate = self
    }
    
    private func configureChatListener() {
        connection?.on(method: Constants.receiveMessage) { [weak self] (
            userId: String,
            displayName: String,
            message: String,
            type: Int,
            attachment: String?
        ) in
            self?.delegate?.didReceiveMessage(
                from: userId,
                displayName: displayName,
                message: message,
                type: type,
                fileType: attachment
            )
        }

        connection?.on(method: Constants.userTyping) { [weak self] (user: String) in
            self?.delegate?.userTyping(username: user)
        }
    }

    // MARK: - Connection Management
    
    private func handleConnectionError(_ error: Error) {
        disconnectionReason = .networkError
        connectionState = .disconnected
        
        if let signalRError = error as? SignalRError {
            delegate?.didDisconnect(error: signalRError)
        } else {
            delegate?.didDisconnect(error: SignalRError.serverClose(message: error.localizedDescription))
        }
        
        if shouldAttemptReconnect {
            handleReconnection()
        }
    }
    
    public func connect(userId: String, groupId: String) {
        self.userId = userId
        self.currentGroup = groupId
        guard connectionState != .connected else { return }
        
        disconnectReason = .none
        
        guard let connection = connection else {
            delegate?.didEncounterError(SignalRError.invalidState)
            return
        }
        
        handleConnection { [weak self] in
            guard let self = self else { return }
            if connection.connectionId == nil {
                connection.start()
            } else if let group = self.currentGroup {
                self.joinGroup(group)
            }
        }
    }

    public func disconnect() {
        disconnectReason = .userInitiated
        isReconnecting = false
        reconnectAttempts = 0
        
        if let group = currentGroup {
            leaveGroup(group)
        }
        
        handleConnection { [weak self] in
            self?.connection?.stop()
        }
        
        currentGroup = nil
        userId = nil
    }

    private func joinGroup(_ group: String) {
        connection?.invoke(method: Constants.joinGroup, arguments: [group]) { [weak self] error in
            if let error = error {
                print("❌ Error joining group: \(error)")
            } else {
                self?.delegate?.didConnect()
                print("✅ Joined group: \(group)")
                self?.retryPendingMessages()
            }
        }
    }

    private func leaveGroup(_ group: String) {
        connection?.invoke(method: Constants.leaveGroup, arguments: [group]) { error in
            if let error = error {
                print("⚠️ Error leaving group: \(error)")
            }
        }
    }

    // MARK: - Messaging
    
    public func sendMessage(type: Int, message: String, fileType: String?) {
        guard let group = currentGroup, let userId = userId else { return }
        
        if connectionState != .connected {
            pendingMessages.append(RetryableMessage(type: type, message: message, fileType: fileType))
            return
        }
        
        connection?.invoke(
            method: Constants.sendMessageToGroup,
            arguments: [group, userId, message, type, fileType ?? ""]
        ) { [weak self] error in
            if let error = error {
                self?.handleMessageError(
                    RetryableMessage(type: type, message: message, fileType: fileType),
                    error: error
                )
            }
        }
    }

    public func notifyTyping() {
        guard let userId = userId else { return }
        connection?.invoke(method: Constants.notifyTyping, arguments: [userId]) { error in
            if let error = error {
                print("⚠️ Error sending typing notification: \(error)")
            }
        }
    }
    
    private func handleMessageError(_ message: RetryableMessage, error: Error) {
        let nextRetry = message.incrementRetry()
        
        if nextRetry.shouldRetry {
            let delay = calculateRetryDelay(attempt: nextRetry.retryCount)
            scheduleMessageRetry(nextRetry, delay: delay)
        } else {
            delegate?.didEncounterError(SignalRError.hubInvocationError(message: error.localizedDescription))
        }
    }

    private func calculateRetryDelay(attempt: Int) -> TimeInterval {
        let delay = Constants.RetryConfig.initialDelay * pow(Constants.RetryConfig.multiplier, Double(attempt - 1))
        return min(delay, Constants.RetryConfig.maxDelay)
    }

    private func scheduleMessageRetry(_ message: RetryableMessage, delay: TimeInterval) {
        DispatchQueue.global().asyncAfter(deadline: .now() + delay) { [weak self] in
            guard let self = self else { return }
            
            if self.isConnected {
                self.sendMessage(
                    type: message.type,
                    message: message.message,
                    fileType: message.fileType
                )
            } else {
                self.pendingMessages.append(message)
            }
        }
    }

    private func retryPendingMessages() {
        let messages = pendingMessages
        pendingMessages.removeAll()
        
        messages.forEach { message in
            sendMessage(type: message.type, message: message.message, fileType: message.fileType)
        }
    }

    // MARK: - **Manual Reconnect for SignalRClient**
    
    private func handleReconnection() {
        guard disconnectReason != .userInitiated,
              !isReconnecting,
              reconnectAttempts < Constants.maxReconnectAttempts,
              let userId = userId,
              let groupId = currentGroup else {
            return
        }
        
        isReconnecting = true
        let delay = calculateRetryDelay(attempt: reconnectAttempts + 1)
        
        connectionQueue.asyncAfter(deadline: .now() + delay) { [weak self] in
            guard let self = self else { return }
            self.reconnectAttempts += 1
            self.connect(userId: userId, groupId: groupId)
        }
    }

    private func handleConnectionSuccess() {
        isReconnecting = false
        reconnectAttempts = 0
        retryPendingMessages()
    }

    public func enterChatScreen() {
        isInChatScreen = true
        disconnectionReason = .none
        
        if connectionState == .disconnected {
            if let userId = userId, let group = currentGroup {
                connect(userId: userId, groupId: group)
            }
        }
    }
    
    public func leaveChatScreen() {
        isInChatScreen = false
        if let group = currentGroup {
            leaveGroup(group)
        }
    }

    public func reconnect() {
        guard !isConnected, !isManualDisconnect else { return }
        handleReconnection()
    }
    
    private func setupBackgroundTask() {
        guard backgroundTask == .invalid else { return }
        
        backgroundTask = UIApplication.shared.beginBackgroundTask { [weak self] in
            print("⏰ Background task time limit reached")
            self?.pauseConnection()
            self?.endBackgroundTask()
        }
    }
    
    private func endBackgroundTask() {
        if backgroundTask != .invalid {
            UIApplication.shared.endBackgroundTask(backgroundTask)
            backgroundTask = .invalid
            print("🏁 Background task ended")
        }
    }

    private func setupBackgroundHandling() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleAppDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleAppWillEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: nil
        )
    }
    
    @objc
    private func handleAppDidEnterBackground() {
        isInBackground = true
        
        if isConnected {
            setupBackgroundTask()
        }
    }

    private func pauseConnection() {
        if isConnected {
            disconnectionReason = .backgroundTimeout
            if let group = currentGroup {
                leaveGroup(group)
            }
            connection?.stop()
            connectionState = .disconnected
        }
    }

    @objc
    private func handleAppWillEnterForeground() {
        isInBackground = false
        endBackgroundTask()
        
        if connectionState == .disconnected && shouldAttemptReconnect {
            if let userId = userId, let group = currentGroup {
                connect(userId: userId, groupId: group)
            }
        }
    }

    private var shouldAttemptReconnect: Bool {
        switch disconnectionReason {
        case .userInitiated:
            return false
        case .networkError:
            return isInChatScreen && !isInBackground
        case .backgroundTimeout:
            return isInChatScreen && !isInBackground
        case .none:
            return isInChatScreen
        }
    }

    private func handleConnection(_ operation: @escaping () -> Void) {
        connectionQueue.async { [weak self] in
            guard let self = self else { return }
            if self.connectionState != .connecting {
                self.connectionState = .connecting
                operation()
            }
        }
    }
}

// MARK: - HubConnectionDelegate

extension SignalRServiceLive: HubConnectionDelegate {
    public func connectionDidOpen(hubConnection: HubConnection) {
        connectionState = .connected
        configureChatListener()
        delegate?.didConnect()
        handleConnectionSuccess()
        
        if let group = currentGroup {
            joinGroup(group)
        }
    }

    public func connectionDidFailToOpen(error: Error) {
        connectionState = .disconnected
        disconnectReason = .networkError
        delegate?.didDisconnect(error: error)
        handleReconnection()
    }

    public func connectionDidClose(error: Error?) {
        connectionState = .disconnected
        if disconnectReason == .none {
            disconnectReason = .networkError
        }
        if let error = error {
            delegate?.didDisconnect(error: error)
        } else {
            delegate?.didDisconnect(error: SignalRError.serverClose(message: nil))
        }
        handleReconnection()
    }
}
