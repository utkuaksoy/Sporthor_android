//
//  CrateChatRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//
//

import ChatKit
import ChatCoordinator
import Factory
import UIKit

final class CreateChatRouter: BaseRouter {}

// MARK: - CreateChatRouterProtocol

extension CreateChatRouter: CreateChatRouterProtocol {
    func handleRouter(_ router: CreateChatRoutes) {
        switch router {
        case .createGroup:
            let vc = CreateGroupChatBuilder.build(with: .newGroup)
            self.viewController.navigationController?.pushViewController(vc, animated: true)
        case .createCommunity: break
        case .connectContacts: break
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

extension CreateChatRouter: ChatCoordinatorDelegate {
    
    func navigateToProfile(userId: String, userName: String, isGroup: Bool, groupId: String?) {
        DispatchQueue.main.async {
            guard let navigationController = UIApplication.shared.activeNavigationController,
            let groupId else { return }
            let controller = ChatUserInfoBuilder.build(userId: userId, groupId: groupId)
            navigationController.pushViewController(controller, animated: true)
        }
    }
    
    func navigateToBack() {
        guard let navigationController = UIApplication.shared.activeNavigationController else { return }
        if let targetVC = navigationController.viewControllers.first(where: { $0 is MessagesViewController }) {
            navigationController.popToViewController(targetVC, animated: true)
        }
    }
}
