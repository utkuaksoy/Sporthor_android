//
//  PostSettingRouter.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation
import PanModal

final class PostSettingRouter: BaseRouter {}

// MARK: - PostSettingRouterProtocol
extension PostSettingRouter: PostSettingRouterProtocol {
    func handleRouter(_ router: PostSettingRoutes) {
        switch router {
        case .complain(let delegate, let model):
            let vc = ComplainBuilder.build(delegate: delegate, model: model)
            viewController.presentPanModal(vc)
        case .dismissHideOrRemove(let delegate, let post):
            viewController.dismiss(animated: true) {
                delegate?.dismissHideOrRemove(model: post)
            }
        case .blockUser(delegate: let delegate, post: let post):
            viewController.dismiss(animated: true) {
                delegate?.didBlockUser(model: post)
            }
        }
    }
}
