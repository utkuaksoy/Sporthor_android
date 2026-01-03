//
//  EditGroupNameRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import UIKit

final class EditGroupNameRouter: BaseRouter {}

// MARK: - EditGroupNameRouterProtocol
extension EditGroupNameRouter: EditGroupNameRouterProtocol {
    func handleRouter(_ router: EditGroupNameRoutes) {
        switch router {
        case .dismiss:
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            if let targetVC = navigationController.viewControllers.first(where: { $0 is MessagesViewController }) {
                navigationController.popToViewController(targetVC, animated: true)
            }
        }
    }
}
