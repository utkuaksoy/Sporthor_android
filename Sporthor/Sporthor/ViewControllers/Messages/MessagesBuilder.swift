//
//  MessagesBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 23.03.2025.
//
//

import Foundation

final class MessagesBuilder {
    static func build() -> MessagesViewController {
        let view = MessagesViewController()
        let interactor = MessagesInteractor()
        let router = MessagesRouter(viewController: view)
        let presenter = MessagesPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
