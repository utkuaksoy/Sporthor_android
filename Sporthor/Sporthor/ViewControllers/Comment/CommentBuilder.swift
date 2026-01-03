//
//  CommentBuilder.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//
//

import Foundation

final class CommentBuilder {
    static func build(postId: String,
                      delegate: CommentViewDelegate? = nil) -> CommentViewController {
        let view = CommentViewController()
        let interactor = CommentInteractor()
        let router = CommentRouter(viewController: view)
        let presenter = CommentPresenter(view: view,
                                         interactor: interactor,
                                         router: router,
                                         postId: postId,
                                         delegate: delegate)
        view.presenter = presenter
        return view
    }
}
