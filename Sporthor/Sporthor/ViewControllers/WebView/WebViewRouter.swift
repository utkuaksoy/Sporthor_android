//
//  WebViewRouter.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

final class WebViewRouter: BaseRouter {}

// MARK: - WebViewRouterProtocol
extension WebViewRouter: WebViewRouterProtocol {
    func handleRouter(_ router: WebViewRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .dismiss:
            viewController.dismiss(animated: true)
        }
    }
}
