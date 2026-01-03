//
//  CalendarAddMissionTypeSelectorRouter.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//
//

import Foundation

final class CalendarAddMissionTypeSelectorRouter: BaseRouter {}

// MARK: - CalendarAddMissionTypeSelectorRouterProtocol
extension CalendarAddMissionTypeSelectorRouter: CalendarAddMissionTypeSelectorRouterProtocol {
    func handleRouter(_ router: CalendarAddMissionTypeSelectorRoutes) {
        switch router {
        case .didBackCalendarAddMission(delegate: let delegate):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didTappedCalendarAddMission()
            }
        case .didBackCalendarBookmark(delegate: let delegate):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didTappedCalendarBookmark()
            }
        }
    }
}
