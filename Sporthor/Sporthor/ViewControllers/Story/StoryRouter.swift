//
//  StoryRouter.swift
//  Sporthor
//
//  Created by derTurke on 28.04.2025.
//
//

import Foundation
import PanModal

final class StoryRouter: BaseRouter {}

// MARK: - StoryRouterProtocol
extension StoryRouter: StoryRouterProtocol {
    func handleRouter(_ router: StoryRoutes) {
        switch router {
        case .dismiss(delegate: let delegate,
                      model: let model,
                      actionType: let actionType,
                      animated: let animated):
            viewController.dismiss(animated: animated) {
                delegate?.didDismissStories()
            }
        case .storySetting(delegate: let delegate, storyDetail: let model):
            let vc = StorySettingBuilder.build(delegate: delegate, model: model)
            viewController.presentPanModal(vc)
        }
    }
}
