//
//  ExperienceJobBuilder.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation

final class ExperienceJobBuilder {
    static func build(isEdit: Bool = false) -> ExperienceJobViewController {
        let view = ExperienceJobViewController()
        let interactor = ExperienceJobInteractor()
        let router = ExperienceJobRouter(viewController: view)
        let presenter = ExperienceJobPresenter(view: view,
                                               interactor: interactor,
                                               router: router,
                                               isEdit: isEdit)
        view.presenter = presenter
        return view
    }
}
