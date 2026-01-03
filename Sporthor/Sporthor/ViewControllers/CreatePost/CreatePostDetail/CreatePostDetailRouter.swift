//
//  CreatePostDetailRouter.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//
//

import Foundation

final class CreatePostDetailRouter: BaseRouter {}

// MARK: - CreatePostDetailRouterProtocol
extension CreatePostDetailRouter: CreatePostDetailRouterProtocol {
    func handleRouter(_ router: CreatePostDetailRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .dismiss:
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                NotificationCenter.default.post(name: .addPost, object: nil)
            }
        }
    }
}
