//
//  NotificationRouter.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//
//

import UIKit
import Factory
import ChatKit
import ChatCoordinator

final class NotificationRouter: BaseRouter {}

// MARK: - NotificationRouterProtocol
extension NotificationRouter: NotificationRouterProtocol {
    func handleRouter(_ router: NotificationRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .openProfile(userId: let userId, username: let username):
            let vc = ProfileBuilder.build(userId: userId, userName: username)
            viewController.show(vc, sender: nil)
            
        case .chat(
            userId: let userId,
            displayName: let displayName,
            image: let image,
            isGroup: let isGroup,
            toUserId: let toUserId
        ):
            guard let navigationController = viewController.navigationController else { return }
            Container.shared.chatCoordinator()?.start(
                navigationController: navigationController,
                delegate: self,
                chatPartner: .init(
                    senderId: userId ?? "",
                    displayName: displayName ?? "",
                    image: image,
                    isGroup: isGroup,
                    toUserId: toUserId
                )
            )
        }
    }
}

extension NotificationRouter: ChatCoordinatorDelegate {
    func navigateToProfile(userId: String, userName: String, isGroup: Bool, groupId: String?) {
        DispatchQueue.main.async {
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            if isGroup {
                let controller = GroupDetailBuilder.build(groupId: userId, groupName: userName, groupImage: "")
                navigationController.pushViewController(controller, animated: true)
            } else {
                guard let groupId else { return }
                let controller = ChatUserInfoBuilder.build(userId: userId, groupId: groupId)
                navigationController.pushViewController(controller, animated: true)
            }
        }
    }
}
