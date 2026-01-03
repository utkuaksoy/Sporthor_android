//
//  CreateTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class CreateTrainingGroupBuilder {
    static func build(selectedTeams: [TeamItemModel] = [],
                      model: GetTrainingGroupUserModel? = nil,
                      isLogin: Bool = false) -> CreateTrainingGroupViewController {
        let view = CreateTrainingGroupViewController()
        let interactor = CreateTrainingGroupInteractor()
        let router = CreateTrainingGroupRouter(viewController: view)
        let presenter = CreateTrainingGroupPresenter(view: view,
                                                     interactor: interactor,
                                                     router: router,
                                                     selectedTeams: selectedTeams,
                                                     model: model,
                                                     isLogin: isLogin)
        view.presenter = presenter
        return view
    }
}
