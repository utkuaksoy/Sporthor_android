//
//  CalendarAddMissionRouter.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//
//

import Foundation
import PanModal

final class CalendarAddMissionRouter: BaseRouter {}

// MARK: - CalendarAddMissionRouterProtocol
extension CalendarAddMissionRouter: CalendarAddMissionRouterProtocol {
    func handleRouter(_ router: CalendarAddMissionRoutes) {
        switch router {
        case .back(let delegate):
            viewController.navigationController?.popViewController(animated: true)
            delegate?.didCalendarAddMission()
        case .date(let selectedDate, let delegate):
            let vc = CalendarModalDateBuilder.build(
                selectedDate: selectedDate,
                delegate: delegate
            )
            viewController.presentPanModal(vc)
        case .hour(selectedHour: let selectedHour, delegate: let delegate):
            let vc = CalendarModalHourBuilder.build(
                delegate: delegate,
                selectedHour: selectedHour
            )
            viewController.presentPanModal(vc)
        case .addEventType(let delegate):
            let vc = CalendarAddEventTypeBuilder.build(delegate: delegate)
            viewController.presentPanModal(vc)
        case .addDescription(delegate: let delegate, description: let description):
            let vc = CalendarAddDescriptionBuilder.build(delegate: delegate,
                                                         description: description)
            viewController.presentPanModal(vc)
        case .addPersonOrGroup(delegate: let delegate,
                               trainingGroup: let trainingGroup,
                               users: let users):
            let vc = CalendarAddPersonOrGroupBuilder.build(delegate: delegate, trainingGroup: trainingGroup, users: users)
            viewController.presentPanModal(vc)
        case .mapView(let delegate, let placemark):
            let vc = CalendarMapViewBuilder.build(delegate: delegate,
                                                  placemark: placemark)
            viewController.presentPanModal(vc)
        }
    }
}
