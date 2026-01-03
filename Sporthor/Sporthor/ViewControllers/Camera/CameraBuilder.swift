//
//  CameraBuilder.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import Foundation

final class CameraBuilder {
    static func build(feedType: FeedType = .post, previewDelegate: PreviewDelegate? = nil) -> CameraViewController {
        let view = CameraViewController()
        let interactor = CameraInteractor()
        let router = CameraRouter(viewController: view)
        let presenter = CameraPresenter(view: view,
                                        interactor: interactor,
                                        router: router,
                                        feedType: feedType,
                                        previewDelegate: previewDelegate)
        view.presenter = presenter
        return view
    }
}
