//
//  PreviewRouter.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import Foundation

final class PreviewRouter: BaseRouter {}

// MARK: - PreviewRouterProtocol
extension PreviewRouter: PreviewRouterProtocol {
    func handleRouter(_ router: PreviewRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: false)
        case .backLibrary:
            if let targetVC = viewController.navigationController?.viewControllers.first(where: { $0 is CreatePostViewController }) {
                viewController.navigationController?.popToViewController(targetVC, animated: true)
            }
        case .createPostDetail(let model):
            let vc = CreatePostDetailBuilder.build(model)
            viewController.show(vc, sender: nil)
        case .dismiss(let previewDelegate):
            viewController.dismiss(animated: true) {
                previewDelegate?.didAddStory()
            }
        case .locations(let delegate):
            let vc = LocationBuilder.build(delegate: delegate)
            let nav = CustomNavigationController(rootViewController: vc)
            nav.modalPresentationStyle = .fullScreen
            viewController.present(nav, animated: true)
        }
    }
}
