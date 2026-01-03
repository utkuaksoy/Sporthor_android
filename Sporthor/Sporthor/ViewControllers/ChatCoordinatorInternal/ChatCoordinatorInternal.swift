//
//  ChatCoordinatorInternal.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 25.04.2025.
//

import ChatKit
import ChatCoordinator
import Factory
import UIKit

final class ChatCoordinatorInternal: ChatCoordinatorDelegate {
    
    static let shared = ChatCoordinatorInternal()
    
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
        navigationController.popViewController(animated: true)
    }
}
