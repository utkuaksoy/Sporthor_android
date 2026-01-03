//
//  NotificationBuilder.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//
//

import Foundation

final class NotificationBuilder {
    static func build() -> NotificationViewController {
        let view = NotificationViewController()
        let interactor = NotificationInteractor()
        let router = NotificationRouter(viewController: view)
        let presenter = NotificationPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
