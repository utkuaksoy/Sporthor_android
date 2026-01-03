//
//  ChatUserInfoRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import Foundation

final class ChatUserInfoRouter: BaseRouter {}

// MARK: - ChatUserInfoRouterProtocol
extension ChatUserInfoRouter: ChatUserInfoRouterProtocol {
    func handleRouter(_ router: ChatUserInfoRoutes) {
        switch router {
        case .profile(userId: let userId, userName: let userName):
            guard let navigationController = viewController.navigationController else { return }
            let vc = ProfileBuilder.build(userId: userId, userName: userName)
            navigationController.pushViewController(vc, animated: true)
        case .media(let groupId):
            guard let navigationController = viewController.navigationController else { return }
            let vc = ChatMediaAndDocumentBuilder.build(groupId: groupId)
            navigationController.pushViewController(vc, animated: true)
        case .clearChat:
            break
        case .createGroup:
            break
        case .dismiss:
            break
        }
    }
}
