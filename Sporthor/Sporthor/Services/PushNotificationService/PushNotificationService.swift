//
//  PushNotificationService.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.04.2025.
//

import AudioToolbox
import Combine
import ChatKit
import Factory
import NetworkKit
import UIKit
import UserNotifications

// MARK: - Types

public enum PushNotificationType {
    case chat(
        messageId: String,
        unReadCount: String,
        lastMessage: String,
        senderName: String,
        imageURL: String,
        groupId: String,
        isGroup: String,
        toUserId: String,
        userName: String,
        sendDate: String,
        type: String
    )
    case like(
        userId: String,
        profilePhoto: String,
        username: String,
        type: String
    )
    case post(
        postId: String,
        userId: String,
        profilePhoto: String,
        username: String,
        type: String
    )
    case follow(
        userId: String,
        username: String,
        type: String
    )
    case unknown(data: [AnyHashable : Any])
    
    var typeString: String {
        switch self {
        case .chat:
            return "0"
        case .like:
            return "1"
        case .follow:
            return "2"
        case .post:
            return "3"
        case .unknown:
            return ""
        }
    }
}

extension PushNotificationType {
    init(typeString: String, data: [AnyHashable: Any]) {
        switch typeString {
        case "0":
            self = .chat(
                messageId: data["messageId"] as? String ?? "",
                unReadCount: data["unReadCount"] as? String ?? "",
                lastMessage: data["lastMessage"] as? String ?? "",
                senderName: data["senderName"] as? String ?? "",
                imageURL: data["imageURL"] as? String ?? "",
                groupId: data["groupId"] as? String ?? "",
                isGroup: data["isGroup"] as? String ?? "",
                toUserId: data["toUserId"] as? String ?? "",
                userName: data["userName"] as? String ?? "",
                sendDate: data["sendDate"] as? String ?? "",
                type: data["type"] as? String ?? ""
            )
        case "1":
            self = .like(
                userId: data["userId"] as? String ?? "",
                profilePhoto: data["profilePhoto"] as? String ?? "",
                username: data["username"] as? String ?? "",
                type: data["type"] as? String ?? ""
            )
        case "2":
            self = .follow(
                userId: data["userId"] as? String ?? "",
                username: data["username"] as? String ?? "",
                type: data["type"] as? String ?? ""
            )
        case "3":
            self = .post(
                postId: data["postId"] as? String ?? "",
                userId: data["userId"] as? String ?? "",
                profilePhoto: data["profilePhoto"] as? String ?? "",
                username: data["username"] as? String ?? "",
                type: data["type"] as? String ?? ""
            )
        default:
            self = .unknown(data: data)
        }
    }
}

public protocol PushNotificationServiceProtocol {
    var messagePublisher: AnyPublisher<PushMessageData, Never> { get }
    var tokenPublisher: AnyPublisher<String?, Never> { get }
    func handlePushNotification(data: [AnyHashable : Any], isFromPushClick: Bool)
    func updateToken(_ token: String?)
}

// MARK: - PushNotificationService

final class PushNotificationService: PushNotificationServiceProtocol {

    // MARK: - Private Properties
    
    private let messageSubject = PassthroughSubject<PushMessageData, Never>()
    private let pushClickSubject = PassthroughSubject<String, Never>()
    private let tokenSubject = CurrentValueSubject<String?, Never>(nil)
    private var cancellables = Set<AnyCancellable>()
    
    // MARK: - Protocol Properties
    
    var messagePublisher: AnyPublisher<PushMessageData, Never> {
        messageSubject.eraseToAnyPublisher()
    }
    
    var tokenPublisher: AnyPublisher<String?, Never> {
        tokenSubject.eraseToAnyPublisher()
    }
    
    // MARK: - Initialization
    
    init() {
        setupTokenObservation()
    }
    
    // MARK: - Public Methods
    
    func handlePushNotification(data: [AnyHashable : Any], isFromPushClick: Bool = false) {
        let notificationType = determineNotificationType(from: data)
        
        switch notificationType {
        case .chat(
            let messageId,
            let unReadCount,
            let lastMessage,
            let senderName,
            let imageURL,
            let groupId,
            let isGroup,
            let toUserId,
            let userName,
            let sendDate,
            let type
        ):
            handleChatNotification(
                messageId: messageId,
                unReadCount: isFromPushClick ? "0" : unReadCount,
                lastMessage: lastMessage,
                senderName: senderName,
                imageURL: imageURL,
                groupId: groupId,
                isGroup: isGroup,
                toUserId: toUserId,
                userName: userName,
                sendDate: sendDate,
                type: type,
                isFromPushClick: isFromPushClick
            )
        case .unknown, .like, .follow, .post:
            break
        }
    }
    
    func updateToken(_ token: String?) {
        tokenSubject.send(token)
    }
    
    // MARK: - Private Methods
    
    private func handleChatNotification(
        messageId: String,
        unReadCount: String,
        lastMessage: String,
        senderName: String,
        imageURL: String,
        groupId: String,
        isGroup: String,
        toUserId: String,
        userName: String,
        sendDate: String,
        type: String,
        isFromPushClick: Bool = false
    ) {
        let messageData = PushMessageData(
            messageId: messageId,
            unReadCount: isFromPushClick ? "0" : unReadCount,
            lastMessage: lastMessage,
            senderName: senderName,
            imageURL: imageURL,
            groupId: groupId,
            isGroup: isGroup,
            toUserId: toUserId,
            userName: userName,
            sendDate: sendDate,
            type: type
        )
        
        messageSubject.send(messageData)
        
        if !isFromPushClick && UIApplication.shared.applicationState == .active && !isUserInChatScreen(for: groupId) {
            showLocalNotification(for: messageData)
        }
    }
    
    private func determineNotificationType(from data: [AnyHashable: Any]) -> PushNotificationType {
        guard let type = data["type"] as? String else { return .unknown(data: data) }
        let messageType = PushNotificationType(typeString: type, data: data)
        return messageType
    }
    
    private func isUserInChatScreen(for groupId: String) -> Bool {
        guard let rootViewController = UIApplication.shared.topViewController() else { return false }
        if let chatVC = rootViewController as? ChatViewControllerProtocol,
           chatVC.senderId == groupId {
            return true
        }
        return false
    }
    
    private func showLocalNotification(for messageData: PushMessageData) {
        UNUserNotificationCenter.current().getNotificationSettings { [weak self] settings in
            guard let self = self else { return }
            
            switch settings.authorizationStatus {
            case .authorized, .provisional:
                self.createLocalNotification(for: messageData)
            case .denied:
                print("❌ Notification permission denied")
            case .notDetermined:
                self.requestNotificationPermission { granted in
                    if granted {
                        self.createLocalNotification(for: messageData)
                    }
                }
            case .ephemeral:
                print("❌ Notification permission denied")
            @unknown default:
                break
            }
        }
    }
    
    private func requestNotificationPermission(completion: @escaping (Bool) -> Void) {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            DispatchQueue.main.async {
                if let error = error {
                    print("❌ Notification permission error: \(error.localizedDescription)")
                    completion(false)
                } else {
                    completion(granted)
                }
            }
        }
    }
    
    private func createLocalNotification(for messageData: PushMessageData) {
        let content = UNMutableNotificationContent()
        content.title = messageData.senderName
        content.body = messageData.lastMessage
        content.sound = .default
        content.userInfo = createNotificationUserInfo(from: messageData)
        
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
        
        createNotification(content: content, with: nil)
    }
    
    private func createNotificationUserInfo(from messageData: PushMessageData) -> [String: Any] {
        [
            "messageId": messageData.messageId,
            "unReadCount": messageData.unReadCount,
            "lastMessage": messageData.lastMessage,
            "senderName": messageData.senderName,
            "imageURL": messageData.imageURL,
            "groupId": messageData.groupId,
            "isGroup": messageData.isGroup,
            "toUserId": messageData.toUserId,
            "type": messageData.type,
            "origin": "local"
        ]
    }
    
    private func createNotification(content: UNMutableNotificationContent, with attachment: UNNotificationAttachment?) {
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 0.1, repeats: false)
        
        let request = UNNotificationRequest(
            identifier: UUID().uuidString,
            content: content,
            trigger: trigger
        )
        
        UNUserNotificationCenter.current().add(request) { error in
            if let error = error {
                print("❌ Bildirim gösterilemedi: \(error.localizedDescription)")
            } else {
                print("✅ Bildirim başarıyla gösterildi.")
            }
        }
    }
    
    private func setupTokenObservation() {
        tokenPublisher
            .compactMap { $0 }
            .removeDuplicates()
            .sink { [weak self] token in
                Task {
                    await self?.sendTokenToServer(token)
                }
            }
            .store(in: &cancellables)
    }
    
    private func sendTokenToServer(_ token: String) async {
        guard let networkManager = Container.shared.networkManager() else { return }
        let request = PushNotificationNetworkTask.pushToken(fcmToken: .init(firebaseToken: token))
        let result = await networkManager.request(service: request, responseType: EmptyModel.self)
        switch result {
        case .success:
            print("✅ FCM Token başarıyla gönderildi: \(token)")
        case .failure(let error):
            print("❌ FCM Token gönderilemedi: \(error.localizedDescription)")
        }
    }
}

public extension Container {
    var pushNotificationService: Factory<PushNotificationServiceProtocol> {
        self { PushNotificationService() }.singleton
    }
}
