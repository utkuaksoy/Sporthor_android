//
//  CalendarModalHourBuilder.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarModalHourBuilder {
    static func build(delegate: CalendarModalHourDelegate? = nil,
                      selectedHour: String? = nil) -> CalendarModalHourViewController {
        let view = CalendarModalHourViewController()
        let interactor = CalendarModalHourInteractor()
        let router = CalendarModalHourRouter(viewController: view)
        let presenter = CalendarModalHourPresenter(view: view, interactor: interactor, router: router, delegate: delegate, selectedHour: selectedHour)
        view.presenter = presenter
        return view
    }
}
