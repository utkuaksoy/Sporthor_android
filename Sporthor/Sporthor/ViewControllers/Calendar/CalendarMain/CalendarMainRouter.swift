//
//  CalendarMainRouter.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation
import PanModal

final class CalendarMainRouter: BaseRouter {}

// MARK: - CalendarMainRouterProtocol
extension CalendarMainRouter: CalendarMainRouterProtocol {
    func handleRouter(_ router: CalendarMainRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .calendarDetail(date: let date):
            let vc = CalendarDetailBuilder.build(date: date)
            viewController.show(vc, sender: nil)
        case .addMission(let delegate):
            let vc = CalendarAddMissionBuilder.build(delegate: delegate)
            viewController.show(vc, sender: nil)
        case .addMissionTypeSelector(delegate: let delegate):
            let vc = CalendarAddMissionTypeSelectorBuilder.build(delegate: delegate)
            viewController.presentPanModal(vc)
        case .calendarBookmark(delegate: let delegate):
            let vc = CalendarMissionDraftBuilder.build(delegate: delegate)
            viewController.show(vc, sender: nil)
        }
    }
}
