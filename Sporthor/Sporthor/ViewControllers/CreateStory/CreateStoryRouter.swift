//
//  CreateStoryRouter.swift
//  Sporthor
//
//  Created by derTurke on 26.04.2025.
//
//

import Foundation

final class CreateStoryRouter: BaseRouter {}

// MARK: - CreateStoryRouterProtocol
extension CreateStoryRouter: CreateStoryRouterProtocol {
    func handleRouter(_ router: CreateStoryRoutes) {
        switch router {
        case .showAlertConroller(let alertController):
            viewController.present(alertController, animated: true)
        case .close:
            viewController.dismiss(animated: true)
        case .camera(let previewDelegate):
            let vc = CameraBuilder.build(feedType: .story, previewDelegate: previewDelegate)
            viewController.show(vc, sender: nil)
        case .preview(let image, let video, let feedType, let previewDelegate):
            let vc = PreviewBuilder.build(image: image,
                                          video: video,
                                          feedType: feedType,
                                          previewDelegate: previewDelegate)
            viewController.navigationController?.pushViewController(vc, animated: false)
        }
    }
}
