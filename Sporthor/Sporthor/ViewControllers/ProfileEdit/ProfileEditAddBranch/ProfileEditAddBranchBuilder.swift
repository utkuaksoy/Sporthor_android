//
//  ProfileEditAddBranchBuilder.swift
//  Sporthor
//
//  Created by derTurke on 19.04.2025.
//
//

import Foundation

final class ProfileEditAddBranchBuilder {
    static func build(delegate: ProfileEditAddBranchDelegate? = nil,
                      title: String = "",
                      model: [SelectionModel] = [],
                      isSingleSelection: Bool = false) -> ProfileEditAddBranchViewController {
        let view = ProfileEditAddBranchViewController()
        let interactor = ProfileEditAddBranchInteractor()
        let router = ProfileEditAddBranchRouter(viewController: view)
        let presenter = ProfileEditAddBranchPresenter(view: view,
                                                      interactor: interactor,
                                                      router: router,
                                                      delegate: delegate,
                                                      title: title,
                                                      model: model,
                                                      isSingleSelection: isSingleSelection)
        view.presenter = presenter
        return view
    }
}
