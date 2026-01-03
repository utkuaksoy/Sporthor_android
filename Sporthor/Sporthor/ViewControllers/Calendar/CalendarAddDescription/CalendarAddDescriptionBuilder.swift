//
//  CalendarAddDescriptionBuilder.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddDescriptionBuilder {
    static func build(delegate: CalendarAddDescriptionDelegate? = nil,
                      description: String = "") -> CalendarAddDescriptionViewController {
        let view = CalendarAddDescriptionViewController()
        let interactor = CalendarAddDescriptionInteractor()
        let router = CalendarAddDescriptionRouter(viewController: view)
        let presenter = CalendarAddDescriptionPresenter(view: view, interactor: interactor, router: router, delegate: delegate, descriptionText: description)
        view.presenter = presenter
        return view
    }
}
