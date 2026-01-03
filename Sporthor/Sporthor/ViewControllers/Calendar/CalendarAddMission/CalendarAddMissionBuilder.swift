//
//  CalendarAddMissionBuilder.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//
//

import Foundation

final class CalendarAddMissionBuilder {
    static func build(delegate: CalendarAddMissionDelegate? = nil,
                      model: GetCalendarDetailTaskModel? = nil,
                      isEdit: Bool = false,
                      isDraftEdit: Bool = false) -> CalendarAddMissionViewController {
        let view = CalendarAddMissionViewController()
        let interactor = CalendarAddMissionInteractor()
        let router = CalendarAddMissionRouter(viewController: view)
        let presenter = CalendarAddMissionPresenter(view: view,
                                                    interactor: interactor,
                                                    router: router,
                                                    delegate: delegate,
                                                    model: model,
                                                    isEdit: isEdit,
                                                    isDraftEdit: isDraftEdit)
        view.presenter = presenter
        return view
    }
}
