//
//  StoryBuilder.swift
//  Sporthor
//
//  Created by derTurke on 28.04.2025.
//
//

import Foundation

final class StoryBuilder {
    static func build(delegate: StoryDelegate? = nil, model: Story) -> StoryViewController {
        let view = StoryViewController()
        let interactor = StoryInteractor()
        let router = StoryRouter(viewController: view)
        let presenter = StoryPresenter(view: view,
                                       interactor: interactor,
                                       router: router,
                                       delegate: delegate,
                                       model: model)
        view.presenter = presenter
        return view
    }
}
