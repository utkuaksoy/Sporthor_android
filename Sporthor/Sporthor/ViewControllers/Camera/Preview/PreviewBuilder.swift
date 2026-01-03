//
//  PreviewBuilder.swift
//  Sporthor
//
//  Created by derTurke on 24.04.2025.
//
//

import UIKit

final class PreviewBuilder {
    static func build(image: UIImage? = nil,
                      video: URL? = nil,
                      feedType: FeedType = .post,
                      previewDelegate: PreviewDelegate? = nil) -> PreviewViewController {
        let view = PreviewViewController()
        let interactor = PreviewInteractor()
        let router = PreviewRouter(viewController: view)
        let presenter = PreviewPresenter(view: view,
                                         interactor: interactor,
                                         router: router,
                                         image: image,
                                         video: video,
                                         feedType: feedType,
                                         previewDelegate: previewDelegate)
        view.presenter = presenter
        return view
    }
}
