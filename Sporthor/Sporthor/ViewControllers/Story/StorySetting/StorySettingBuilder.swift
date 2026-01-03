//
//  StorySettingBuilder.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//
//

import Foundation

final class StorySettingBuilder {
    static func build(delegate: StorySettingDelegate? = nil,
                      model: StoryDetail) -> StorySettingViewController {
        let view = StorySettingViewController()
        let interactor = StorySettingInteractor()
        let router = StorySettingRouter(viewController: view)
        let presenter = StorySettingPresenter(view: view,
                                              interactor: interactor,
                                              router: router,
                                              delegate: delegate,
                                              model: model)
        view.presenter = presenter
        return view
    }
}
