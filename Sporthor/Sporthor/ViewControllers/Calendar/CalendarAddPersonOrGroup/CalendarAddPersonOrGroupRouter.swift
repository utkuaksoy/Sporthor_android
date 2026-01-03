//
//  CalendarAddPersonOrGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddPersonOrGroupRouter: BaseRouter {}

// MARK: - CalendarAddPersonOrGroupRouterProtocol
extension CalendarAddPersonOrGroupRouter: CalendarAddPersonOrGroupRouterProtocol {
    func handleRouter(_ router: CalendarAddPersonOrGroupRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, persons: let persons, groups: let groups):
            viewController.dismiss(animated: true) {
                delegate?.didSelectCalendarAddPersonOrGroup(persons: persons, groups: groups)
            }
        }
    }
}
