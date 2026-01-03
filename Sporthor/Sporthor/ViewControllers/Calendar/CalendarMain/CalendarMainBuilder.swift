//
//  CalendarMainBuilder.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation

final class CalendarMainBuilder {
    static func build() -> CalendarMainViewController {
        let view = CalendarMainViewController()
        let interactor = CalendarMainInteractor()
        let router = CalendarMainRouter(viewController: view)
        let presenter = CalendarMainPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
