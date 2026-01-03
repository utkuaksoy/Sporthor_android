//
//  CalendarAddDescriptionRouter.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddDescriptionRouter: BaseRouter {}

// MARK: - CalendarAddDescriptionRouterProtocol
extension CalendarAddDescriptionRouter: CalendarAddDescriptionRouterProtocol {
    func handleRouter(_ router: CalendarAddDescriptionRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, description: let description):
            viewController.dismiss(animated: true) {
                delegate?.didAddDescription(description)                
            }
        }
    }
}
