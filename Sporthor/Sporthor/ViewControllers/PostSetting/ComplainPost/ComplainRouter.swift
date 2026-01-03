//
//  ComplainRouter.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation

final class ComplainRouter: BaseRouter {}

// MARK: - ComplainRouterProtocol
extension ComplainRouter: ComplainRouterProtocol {
    func handleRouter(_ router: ComplainRoutes) {
        switch router {
        case .dismiss(delegate: let delegate):
            viewController.dismiss(animated: true) {
                delegate?.dismissComplain()
            }
        }
    }
}
