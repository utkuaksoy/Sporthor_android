//
//  DeeplinkManager.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 24.04.2025.
//

import ChatKit
import ChatCoordinator
import Factory
import UIKit
import Combine

enum DeeplinkType {
    case chat(chatUser: ChatUser)
    case home
    case unknown
}

protocol DeeplinkManagerProtocol {
    func handleDeeplink(_ deeplink: DeeplinkType)
    func handlePushNotification(_ userInfo: [AnyHashable: Any], isColdStart: Bool)
    func homeDidBecomeReady()
}

final class DeeplinkManagerLive: DeeplinkManagerProtocol {
    private var pendingDeeplink: DeeplinkType?
    private var pendingPushNotification: [AnyHashable: Any]?
    private var isHomeReady = false

    func handlePushNotification(_ userInfo: [AnyHashable: Any], isColdStart: Bool) {
        guard
            let messageId = userInfo["messageId"] as? String,
            let senderName = userInfo["senderName"] as? String,
            let imageURL = userInfo["imageURL"] as? String,
            let groupId = userInfo["groupId"] as? String,
            let isGroup = userInfo["isGroup"] as? String,
            let toUserId = userInfo["toUserId"] as? String
        else {
            return
        }
        
        let chatUser = ChatUser(
            senderId: groupId,
            displayName: senderName,
            image: imageURL,
            isGroup: Bool(isGroup),
            toUserId: toUserId
        )
        
        let deeplink = DeeplinkType.chat(chatUser: chatUser)
        
        if isColdStart {
            pendingPushNotification = userInfo
            pendingDeeplink = deeplink
        } else {
            Container.shared.pushNotificationService().handlePushNotification(
                data: userInfo,
                isFromPushClick: true
            )
            handleDeeplink(deeplink)
        }
    }

    func handleDeeplink(_ deeplink: DeeplinkType) {
        if isHomeReady {
            executeDeeplink(deeplink)
        } else {
            pendingDeeplink = deeplink
        }
    }
    
    func homeDidBecomeReady() {
        isHomeReady = true
        
        if let notification = pendingPushNotification {
            handlePushNotification(notification, isColdStart: false)
            pendingPushNotification = nil
        }
        
        if let deeplink = pendingDeeplink {
            executeDeeplink(deeplink)
            pendingDeeplink = nil
        }
    }

    private func executeDeeplink(_ deeplink: DeeplinkType) {
        switch deeplink {
        case .chat(let chatUser):
            if let activeNavigationController = UIApplication.shared.activeNavigationController {
                if let pendingNotification = pendingPushNotification {
                    Container.shared.pushNotificationService().handlePushNotification(
                        data: pendingNotification,
                        isFromPushClick: true
                    )
                    pendingPushNotification = nil
                }
                
                Container.shared.chatCoordinator()?.start(
                    navigationController: activeNavigationController,
                    delegate: ChatCoordinatorInternal.shared,
                    chatPartner: chatUser,
                    isNewCreated: false
                )
            }
        case .home:
            break
        case .unknown:
            break
        }
    }
}

// MARK: - Factory Extension

extension Container {
    var deeplinkManager: Factory<DeeplinkManagerProtocol?> {
        self { DeeplinkManagerLive() }.singleton
    }
}

