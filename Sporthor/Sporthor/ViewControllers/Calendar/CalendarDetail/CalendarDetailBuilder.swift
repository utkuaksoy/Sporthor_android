//
//  CalendarDetailBuilder.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation

final class CalendarDetailBuilder {
    static func build(date: Date = Date()) -> CalendarDetailViewController {
        let view = CalendarDetailViewController()
        let interactor = CalendarDetailInteractor()
        let router = CalendarDetailRouter(viewController: view)
        let presenter = CalendarDetailPresenter(view: view,
                                                interactor: interactor,
                                                router: router,
                                                selectedDate: date)
        view.presenter = presenter
        return view
    }
}
