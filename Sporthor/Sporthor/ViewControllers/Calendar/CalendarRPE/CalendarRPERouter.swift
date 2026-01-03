//
//  CalendarRPERouter.swift
//  Sporthor
//
//  Created by derTurke on 25.07.2025.
//
//

import Foundation

final class CalendarRPERouter: BaseRouter {}

// MARK: - CalendarRPERouterProtocol
extension CalendarRPERouter: CalendarRPERouterProtocol {
    func handleRouter(_ router: CalendarRPERoutes) {
        switch router {
        case .rpeSurvey(delegate: let delegate):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.successRPESurveyCalendarRPE()
            }
        }
    }
}
