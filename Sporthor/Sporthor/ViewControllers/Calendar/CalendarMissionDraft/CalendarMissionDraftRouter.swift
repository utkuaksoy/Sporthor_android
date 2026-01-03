//
//  CalendarMissionDraftRouter.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//
//

import Foundation

final class CalendarMissionDraftRouter: BaseRouter {}

// MARK: - CalendarMissionDraftRouterProtocol
extension CalendarMissionDraftRouter: CalendarMissionDraftRouterProtocol {
    func handleRouter(_ router: CalendarMissionDraftRoutes) {
        switch router {
        case .back(let delegate):
            viewController.navigationController?.popViewController(animated: true)
            delegate?.didCalendarAddMission()
        case .addMission(delegate: let delegate, model: let model):
            let vc = CalendarAddMissionBuilder.build(delegate: delegate,
                                                     model: model,
                                                     isDraftEdit: true)
            viewController.show(vc, sender: nil)
        }
    }
}
