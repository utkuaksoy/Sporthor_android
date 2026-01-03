//
//  AddPersonTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class AddPersonTrainingGroupBuilder {
    static func build(trainingGroup: TrainingGroupResponse,
                      model: GetTrainingGroupUserModel? = nil,
                      isUpdateCoach: Bool = false) -> AddPersonTrainingGroupViewController {
        let view = AddPersonTrainingGroupViewController()
        let interactor = AddPersonTrainingGroupInteractor()
        let router = AddPersonTrainingGroupRouter(viewController: view)
        let presenter = AddPersonTrainingGroupPresenter(view: view,
                                                        interactor: interactor,
                                                        router: router,
                                                        trainingGroup: trainingGroup,
                                                        model: model,
                                                        isUpdateCoach: isUpdateCoach)
        view.presenter = presenter
        return view
    }
}
