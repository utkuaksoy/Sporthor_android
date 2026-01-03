//
//  CalendarMissionDraftBuilder.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//
//

import Foundation

final class CalendarMissionDraftBuilder {
    static func build(delegate: CalendarAddMissionDelegate? = nil) -> CalendarMissionDraftViewController {
        let view = CalendarMissionDraftViewController()
        let interactor = CalendarMissionDraftInteractor()
        let router = CalendarMissionDraftRouter(viewController: view)
        let presenter = CalendarMissionDraftPresenter(view: view,
                                                      interactor: interactor,
                                                      router: router,
                                                      delegate: delegate)
        view.presenter = presenter
        return view
    }
}
