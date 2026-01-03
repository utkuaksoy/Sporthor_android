//
//  EditPersonAndTechnicalStaffTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 31.10.2025.
//
//

import Foundation

final class EditPersonAndTechnicalStaffTrainingGroupBuilder {
    static func build(model: GetTrainingGroupUserModel? = nil,
                      trainingGroup: TrainingGroupResponse,
                      viewType: EditPersonAndTechnicalStaffTrainingGroupViewType = .person,
                      isUpdateCoach: Bool = false) -> EditPersonAndTechnicalStaffTrainingGroupViewController {
        let view = EditPersonAndTechnicalStaffTrainingGroupViewController()
        let interactor = EditPersonAndTechnicalStaffTrainingGroupInteractor()
        let router = EditPersonAndTechnicalStaffTrainingGroupRouter(viewController: view)
        let presenter = EditPersonAndTechnicalStaffTrainingGroupPresenter(
            view: view,
            interactor: interactor,
            router: router,
            model: model,
            trainingGroup: trainingGroup,
            viewType: viewType,
            isUpdateCoach: isUpdateCoach
        )
        view.presenter = presenter
        return view
    }
}
