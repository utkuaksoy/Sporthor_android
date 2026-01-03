//
//  CalendarAddEventTypeBuilder.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarAddEventTypeBuilder {
    static func build(delegate: CalendarAddEventTypeDelegate? = nil) -> CalendarAddEventTypeViewController {
        let view = CalendarAddEventTypeViewController()
        let interactor = CalendarAddEventTypeInteractor()
        let router = CalendarAddEventTypeRouter(viewController: view)
        let presenter = CalendarAddEventTypePresenter(view: view, interactor: interactor, router: router, delegate: delegate)
        view.presenter = presenter
        return view
    }
}
