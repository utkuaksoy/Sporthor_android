//
//  SuccessTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessTrainingGroupBuilder {
    static func build(trainingGroup: TrainingGroupResponse,
                      model: GetTrainingGroupUserModel? = nil) -> SuccessTrainingGroupViewController {
        let view = SuccessTrainingGroupViewController()
        let interactor = SuccessTrainingGroupInteractor()
        let router = SuccessTrainingGroupRouter(viewController: view)
        let presenter = SuccessTrainingGroupPresenter(view: view,
                                                      interactor: interactor,
                                                      router: router,
                                                      trainingGroup: trainingGroup,
                                                      model: model)
        view.presenter = presenter
        return view
    }
}
