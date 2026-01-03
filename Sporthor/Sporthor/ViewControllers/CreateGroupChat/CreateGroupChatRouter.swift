//
//  CreateGroupChatRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import ChatCoordinator
import ChatKit
import Factory
import UIKit

final class CreateGroupChatRouter: BaseRouter {}

// MARK: - CreateGroupChatRouterProtocol

extension CreateGroupChatRouter: CreateGroupChatRouterProtocol {
    func handleRouter(_ router: CreateGroupChatRoutes) {
        switch router {
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
                ),
                isNewCreated: isNewCreated
            )
        }
    }
}

extension CreateGroupChatRouter: ChatCoordinatorDelegate {
    func navigateToProfile(userId: String, userName: String, isGroup: Bool, groupId: String?) {
        guard let navigationController = UIApplication.shared.activeNavigationController else { return }
        let controller = GroupDetailBuilder.build(groupId: userId, groupName: userName, groupImage: "")
        navigationController.pushViewController(controller, animated: true)
    }
    
    func navigateToBack() {
        guard let navigationController = UIApplication.shared.activeNavigationController else { return }
        if let targetVC = navigationController.viewControllers.first(where: { $0 is MessagesViewController }) {
            navigationController.popToViewController(targetVC, animated: true)
        }
    }
}
