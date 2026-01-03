//
//  ChatViewModelProtocols.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 18.02.2025.
//

import ChatKit
import MessageKit
import UIKit

protocol ChatViewModelDelegate: AnyObject {
    func reloadData()
    func didReceiveMessage()
    func showLoading(_ show: Bool)
    func didUpdateTypingStatus(isTyping: Bool, username: String)
    func didUpdateConnectionStatus(isConnected: Bool)
    func didEncounterError(_ error: Error)
}

// MARK: - Output
protocol ChatViewModelOutput {
    var messageCount: Int { get }
    var currentUser: ChatUser { get }
    var chatPartner: ChatUser { get }
    var hasMoreMessages: Bool { get }
    var isNewCreated: Bool { get }
    var users: [UserModel]? { get }
    
    func message(at index: Int) -> ChatMessage
}

// MARK: - Input
protocol ChatViewModelInput {
    func viewDidLoad()
    func viewWillAppear()
    func viewWillDisappear()
    func sendTextMessage(_ text: String)
    func sendImage(_ image: UIImage, imageType: String)
    func sendVideo(url: URL, videoType: String)
    func sendFile(fileUrl: URL)
    func notifyTyping()
    func loadPreviousMessages(completion: @escaping (Bool) -> Void)
}

protocol ChatViewModelProtocol: ChatViewModelInput, ChatViewModelOutput {
    var delegate: ChatViewModelDelegate? { get set }
}
