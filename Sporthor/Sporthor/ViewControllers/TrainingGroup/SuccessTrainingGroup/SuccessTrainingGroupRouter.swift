//
//  SuccessTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessTrainingGroupRouter: BaseRouter {}

// MARK: - SuccessTrainingGroupRouterProtocol
extension SuccessTrainingGroupRouter: SuccessTrainingGroupRouterProtocol {
    func handleRouter(_ router: SuccessTrainingGroupRoutes) {
        switch router {
        case .addPersonTraining(let trainingGroup, let model):
            let vc = AddPersonAndTechnicalStaffTrainingGroupBuilder.build(trainingGroup: trainingGroup, model: model)
            viewController.show(vc, sender: nil)
        case .editPersonTraining(let trainingGroup, let model):
            let vc = EditPersonAndTechnicalStaffTrainingGroupBuilder.build(model: model, trainingGroup: trainingGroup)
            viewController.show(vc, sender: nil)
        }
    }
}
