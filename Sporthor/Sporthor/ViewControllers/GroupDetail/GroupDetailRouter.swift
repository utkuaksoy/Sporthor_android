//
//  GroupDetailRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import UIKit

final class GroupDetailRouter: BaseRouter {}

// MARK: - GroupDetailRouterProtocol
extension GroupDetailRouter: GroupDetailRouterProtocol {
    func handleRouter(_ router: GroupDetailRoutes) {
        switch router {
        case .editGroup(let groupId):
            guard let navigationController = viewController.navigationController else { return }
            let vc = EditGroupNameBuilder.build(groupId: groupId)
            navigationController.pushViewController(vc, animated: true)
        case .profile(let userId, let userName):
            guard let navigationController = viewController.navigationController else { return }
            let vc = ProfileBuilder.build(userId: userId, userName: userName)
            navigationController.pushViewController(vc, animated: true)
        case .returnMessages:
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            if let targetVC = navigationController.viewControllers.first(where: { $0 is MessagesViewController }) {
                navigationController.popToViewController(targetVC, animated: true)
            }
        case .mediaAndDocument(let groupId):
            guard let navigationController = viewController.navigationController else { return }
            let vc = ChatMediaAndDocumentBuilder.build(groupId: groupId)
            navigationController.pushViewController(vc, animated: true)
        }
    }
} 
