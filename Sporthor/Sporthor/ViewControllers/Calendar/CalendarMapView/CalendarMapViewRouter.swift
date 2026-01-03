//
//  CalendarMapViewRouter.swift
//  Sporthor
//
//  Created by derTurke on 9.06.2025.
//
//

import Foundation

final class CalendarMapViewRouter: BaseRouter {}

// MARK: - CalendarMapViewRouterProtocol
extension CalendarMapViewRouter: CalendarMapViewRouterProtocol {
    func handleRouter(_ router: CalendarMapViewRoutes) {
        switch router {
        case .dismiss(delegate: let delegate, placemark: let placemark):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didSelectMapViewLocation(placemark)
            }
        }
    }
}
