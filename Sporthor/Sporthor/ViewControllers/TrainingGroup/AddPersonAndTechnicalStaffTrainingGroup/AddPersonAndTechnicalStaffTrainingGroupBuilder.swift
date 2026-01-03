//
//  AddPersonAndTechnicalStaffTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 27.10.2025.
//
//

import Foundation

final class AddPersonAndTechnicalStaffTrainingGroupBuilder {
    static func build(
        trainingGroup: TrainingGroupResponse,
        model: GetTrainingGroupUserModel? = nil
    ) -> AddPersonAndTechnicalStaffTrainingGroupViewController {
        let view = AddPersonAndTechnicalStaffTrainingGroupViewController()
        let interactor = AddPersonAndTechnicalStaffTrainingGroupInteractor()
        let router = AddPersonAndTechnicalStaffTrainingGroupRouter(viewController: view)
        let presenter = AddPersonAndTechnicalStaffTrainingGroupPresenter(
            view: view,
            interactor: interactor,
            router: router,
            trainingGroup: trainingGroup,
            model: model
        )
        view.presenter = presenter
        return view
    }
}
