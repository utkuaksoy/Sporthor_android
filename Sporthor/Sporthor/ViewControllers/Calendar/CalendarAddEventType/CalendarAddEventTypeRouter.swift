//
//  CalendarAddEventTypeRouter.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarAddEventTypeRouter: BaseRouter {}

// MARK: - CalendarAddEventTypeRouterProtocol
extension CalendarAddEventTypeRouter: CalendarAddEventTypeRouterProtocol {
    func handleRouter(_ router: CalendarAddEventTypeRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, let model):
            viewController.dismiss(animated: true) {
                delegate?.didAddEventType(model)
            }
        }
    }
}
