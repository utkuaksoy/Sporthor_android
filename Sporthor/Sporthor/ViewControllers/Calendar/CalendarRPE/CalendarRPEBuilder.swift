//
//  CalendarRPEBuilder.swift
//  Sporthor
//
//  Created by derTurke on 25.07.2025.
//
//

import Foundation

final class CalendarRPEBuilder {
    static func build(_ model: GetCalendarDetailTaskModel,
                      delegate: CalendarRPEDelegate? = nil) -> CalendarRPEViewController {
        let view = CalendarRPEViewController()
        let interactor = CalendarRPEInteractor()
        let router = CalendarRPERouter(viewController: view)
        let presenter = CalendarRPEPresenter(view: view,
                                             interactor: interactor,
                                             router: router,
                                             model: model,
                                             delegate: delegate)
        view.presenter = presenter
        return view
    }
}
