//
//  ExperienceBranchBuilder.swift
//  Sporthor
//
//  Created by derTurke on 18.02.2025.
//
//

import Foundation

final class ExperienceBranchBuilder {
    static func build(updateProfileRequest: UpdateProfileRequest? = nil) -> ExperienceBranchViewController {
        let view = ExperienceBranchViewController()
        let interactor = ExperienceBranchInteractor()
        let router = ExperienceBranchRouter(viewController: view)
        let presenter = ExperienceBranchPresenter(view: view,
                                                  interactor: interactor,
                                                  router: router,
                                                  updateProfileRequest: updateProfileRequest)
        view.presenter = presenter
        return view
    }
}
