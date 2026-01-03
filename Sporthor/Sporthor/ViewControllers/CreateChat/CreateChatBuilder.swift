//
//  NewChatBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//
//

import Foundation

final class CreateChatBuilder {
    static func build() -> CreateChatViewController {
        let view = CreateChatViewController()
        let interactor = CreateChatInteractor()
        let router = CreateChatRouter(viewController: view)
        let presenter = CreateChatPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
