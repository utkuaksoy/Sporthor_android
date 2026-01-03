//
//  FollowersRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation
import UIKit

final class FollowersRouter: BaseRouter {}

// MARK: - FollowersRouterProtocol
extension FollowersRouter: FollowersRouterProtocol {
    
    func handleRouter(_ routes: FollowersRoutes) {
        switch routes {
        case .profile(let userId, let userName):
            guard let navigationController = viewController.navigationController else { return }
            let controller = ProfileBuilder.build(userId: userId, userName: userName)
            navigationController.pushViewController(controller, animated: true)
        }
    }
}
