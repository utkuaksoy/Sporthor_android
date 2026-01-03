//
//  StorySettingRouter.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//
//

import Foundation

final class StorySettingRouter: BaseRouter {}

// MARK: - StorySettingRouterProtocol
extension StorySettingRouter: StorySettingRouterProtocol {
    func handleRouter(_ router: StorySettingRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, model: let model):
            viewController.dismiss(animated: true) { [weak self] in
                guard let self else { return }
                delegate?.deleteStory(model)
            }
        }
    }
}
