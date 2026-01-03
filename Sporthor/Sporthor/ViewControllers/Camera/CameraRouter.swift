//
//  CameraRouter.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import Foundation

final class CameraRouter: BaseRouter {}

// MARK: - CameraRouterProtocol
extension CameraRouter: CameraRouterProtocol {
    func handleRouter(_ router: CameraRoutes) {
        switch router {
        case .showAlertController(let alertController):
            viewController.present(alertController, animated: true)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .preview(let image, let video, let feedType, let previewDelegate):
            let vc = PreviewBuilder.build(image: image,
                                          video: video,
                                          feedType: feedType,
                                          previewDelegate: previewDelegate)
            viewController.navigationController?.pushViewController(vc, animated: false)
        }
    }
}
