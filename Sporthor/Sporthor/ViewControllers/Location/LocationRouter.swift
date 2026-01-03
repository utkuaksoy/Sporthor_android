//
//  LocationRouter.swift
//  Sporthor
//
//  Created by derTurke on 7.05.2025.
//
//

import Foundation

final class LocationRouter: BaseRouter {}

// MARK: - LocationRouterProtocol
extension LocationRouter: LocationRouterProtocol {
    func handleRouter(_ router: LocationRoutes) {
        switch router {
        case .dismiss(let delegate, let name):
            viewController.dismiss(animated: true) {
                delegate?.didSelectLocation(name: name)
            }
        case .dismissPlacemark(delegate: let delegate, placemark: let placemark):
            viewController.dismiss(animated: true) {
                delegate?.didSelectPlacemark(placemark: placemark)
            }
        case .showAlertController(let alertController):
            viewController.present(alertController, animated: true)
        }
    }
}
