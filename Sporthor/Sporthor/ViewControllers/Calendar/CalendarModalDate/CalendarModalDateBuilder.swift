//
//  CalendarModalDateBuilder.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//
//

import Foundation

final class CalendarModalDateBuilder {
    static func build(
        selectedDate: Date? = nil,
        delegate: CalendarModalDateDelegate? = nil
    ) -> CalendarModalDateViewController {
        let view = CalendarModalDateViewController()
        let interactor = CalendarModalDateInteractor()
        let router = CalendarModalDateRouter(viewController: view)
        let presenter = CalendarModalDatePresenter(view: view,
                                                   interactor: interactor,
                                                   router: router,
                                                   selectedDate: selectedDate,
                                                   delegate: delegate)
        view.presenter = presenter
        return view
    }
}
