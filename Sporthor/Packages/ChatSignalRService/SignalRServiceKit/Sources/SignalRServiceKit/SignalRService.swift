//
//  SignalRService.swift
//  SignalRServiceKit
//
//  Created by Mesut Canbaz on 29.01.2025.
//

import Factory
import Foundation
import SignalRClient

public protocol SignalRServiceDelegate: AnyObject {
    func didReceiveMessage(
        from userId: String,
        displayName: String,
        message: String,
        type: Int,
        fileType: String?
    )
    func userTyping(username: String)
    func didConnect()
    func didDisconnect(error: Error?)
    func didEncounterError(_ error: Error)
}

public extension SignalRServiceDelegate {
    func didUpdateConnectionStatus(isConnected: Bool) {}
}

public protocol SignalRServiceProtocol: AnyObject {
    var connectionState: ConnectionState { get }
    var delegate: SignalRServiceDelegate? { get set }
    var isInChatScreen: Bool { get }
    var isConnected: Bool { get }
    
    func configure(with url: URL)
    func connect(userId: String, groupId: String)
    func disconnect()
    func enterChatScreen()
    func leaveChatScreen()
    func sendMessage(type: Int, message: String, fileType: String?)
    func notifyTyping()
    func reconnect()
}

public extension Container {
    var chatSignalRService: Factory<SignalRServiceProtocol?> {
        self { nil }.singleton
    }
}

