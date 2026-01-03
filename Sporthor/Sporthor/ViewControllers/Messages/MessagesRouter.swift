//
//  MessagesRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.03.2025.
//
//

import ChatKit
import ChatCoordinator
import Factory
import UIKit

final class MessagesRouter: BaseRouter {}

// MARK: - MessagesRouterProtocol
extension MessagesRouter: MessagesRouterProtocol {
    func handleRouter(_ router: MessagesRoutes) {
        switch router {
        case .createChat:
            guard let navigationController = viewController.navigationController else { return }
            let vc = CreateChatBuilder.build()
            navigationController.pushViewController(vc, animated: true)
        case .chat(
            userId: let userId,
            displayName: let displayName,
            image: let image,
            isGroup: let isGroup,
            isNewCreated: let isNewCreated,
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

extension MessagesRouter: ChatCoordinatorDelegate {
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
