//
//  CalendarAddMissionTypeSelectorBuilder.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//
//

import Foundation

final class CalendarAddMissionTypeSelectorBuilder {
    static func build(delegate: CalendarAddMissionTypeSelectorDelegate? = nil) -> CalendarAddMissionTypeSelectorViewController {
        let view = CalendarAddMissionTypeSelectorViewController()
        let interactor = CalendarAddMissionTypeSelectorInteractor()
        let router = CalendarAddMissionTypeSelectorRouter(viewController: view)
        let presenter = CalendarAddMissionTypeSelectorPresenter(view: view,
                                                                interactor: interactor,
                                                                router: router,
                                                                delegate: delegate)
        view.presenter = presenter
        return view
    }
}
