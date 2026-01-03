//
//  CreateTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class CreateTrainingGroupRouter: BaseRouter {}

// MARK: - CreateTrainingGroupRouterProtocol
extension CreateTrainingGroupRouter: CreateTrainingGroupRouterProtocol {
    func handleRouter(_ router: CreateTrainingGroupRoutes) {
        switch router {
        case .home:
            let vc = CustomTabBarController()
            vc.modalTransitionStyle = .crossDissolve
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: true)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .openSelector(title: let title, model: let model, delegate: let delegate):
            let vc = SelectionBuilder.build(title: title, model: model, delegate: delegate)
            viewController.presentPanModal(vc)
        case .successTrainingGroup(let trainingGroup, model: let model):
            let vc = SuccessTrainingGroupBuilder.build(trainingGroup: trainingGroup, model: model)
            viewController.show(vc, sender: nil)
        }
    }
}
