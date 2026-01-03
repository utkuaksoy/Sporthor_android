//
//  ListTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//
//

import Foundation

final class ListTrainingGroupRouter: BaseRouter {}

// MARK: - ListTrainingGroupRouterProtocol
extension ListTrainingGroupRouter: ListTrainingGroupRouterProtocol {
    func handleRouter(_ router: ListTrainingGroupRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .updateTrainingGroup(model: let model):
            let vc = CreateTrainingGroupBuilder.build(model: model, isLogin: true)
            viewController.show(vc, sender: nil)
        }
    }
}
