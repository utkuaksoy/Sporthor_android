//
//  CalendarDetailRouter.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation
import PanModal

final class CalendarDetailRouter: BaseRouter {}

// MARK: - CalendarDetailRouterProtocol
extension CalendarDetailRouter: CalendarDetailRouterProtocol {
    func handleRouter(_ router: CalendarDetailRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .editMission(delegate: let delegate, model: let model):
            let vc = CalendarAddMissionBuilder.build(delegate: delegate,
                                                     model: model,
                                                     isEdit: true)
            viewController.show(vc, sender: nil)
        case .addMission(delegate: let delegate):
            let vc = CalendarAddMissionBuilder.build(delegate: delegate)
            viewController.show(vc, sender: nil)
        case .addMissionTypeSelector(delegate: let delegate):
            let vc = CalendarAddMissionTypeSelectorBuilder.build(delegate: delegate)
            viewController.presentPanModal(vc)
        case .calendarBookmark(delegate: let delegate):
            let vc = CalendarMissionDraftBuilder.build(delegate: delegate)
            viewController.show(vc, sender: nil)
        case .rpe(model: let model, delegate: let delegate):
            let vc = CalendarRPEBuilder.build(model, delegate: delegate)
            viewController.presentPanModal(vc)
        }
    }
}
