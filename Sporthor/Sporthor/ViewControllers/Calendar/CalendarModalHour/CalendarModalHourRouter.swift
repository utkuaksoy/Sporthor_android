//
//  CalendarModalHourRouter.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarModalHourRouter: BaseRouter {}

// MARK: - CalendarModalHourRouterProtocol
extension CalendarModalHourRouter: CalendarModalHourRouterProtocol {
    func handleRouter(_ router: CalendarModalHourRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, selectedHour: let selectedHour):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didSelectHour(selectedHour)
            }
        }
    }
}
