//
//  HomeBuilder.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//
//

import Foundation

final class HomeBuilder {
    static func build(homeModeType: HomeModeType = .home,
                      posts: [Post] = [],
                      profileUserId: String? = nil,
                      initialPostId: String? = nil) -> HomeViewController {
        let view = HomeViewController()
        let interactor = HomeInteractor()
        let router = HomeRouter(viewController: view)
        let presenter = HomePresenter(view: view,
                                      interactor: interactor,
                                      router: router,
                                      homeModeType: homeModeType,
                                      posts: posts,
                                      profileUserId: profileUserId,
                                      initialPostId: initialPostId)
        view.presenter = presenter
        return view
    }
}
