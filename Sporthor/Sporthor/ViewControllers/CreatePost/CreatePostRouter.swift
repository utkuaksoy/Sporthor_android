//
//  CreatePostRouter.swift
//  Sporthor
//
//  Created by derTurke on 21.04.2025.
//
//

import Foundation

final class CreatePostRouter: BaseRouter {}

// MARK: - CreatePostRouterProtocol
extension CreatePostRouter: CreatePostRouterProtocol {
    func handleRouter(_ router: CreatePostRoutes) {
        switch router {
        case .showAlertConroller(let alertController):
            viewController.present(alertController, animated: true)
        case .close:
            viewController.dismiss(animated: true)
        case .openCreatePostDetail(let model):
            let vc = CreatePostDetailBuilder.build(model)
            viewController.show(vc, sender: nil)
        case .camera:
            let vc = CameraBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
