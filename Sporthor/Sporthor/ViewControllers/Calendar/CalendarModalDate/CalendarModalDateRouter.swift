//
//  CalendarModalDateRouter.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//
//

import Foundation

final class CalendarModalDateRouter: BaseRouter {}

// MARK: - CalendarModalDateRouterProtocol
extension CalendarModalDateRouter: CalendarModalDateRouterProtocol {
    func handleRouter(_ router: CalendarModalDateRoutes) {
        switch router {
        case .dismiss(let delegate, let date):
            viewController.dismiss(animated: true) { [weak self] in
                guard let self else { return }
                delegate?.didSelectDate(date)
            }
        }
    }
}
