//
//  AboutRouter.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//
//

import Foundation

final class AboutRouter: BaseRouter {}

// MARK: - AboutRouterProtocol
extension AboutRouter: AboutRouterProtocol {
    func handleRouter(_ router: AboutRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
