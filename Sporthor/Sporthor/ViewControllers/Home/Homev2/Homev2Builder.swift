//
//  Homev2Builder.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//
//

import Foundation

final class Homev2Builder {
    static func build(homeModeType: HomeModeType = .home,
                      posts: [Post] = [],
                      profileUserId: String? = nil,
                      initialPostId: String? = nil) -> Homev2ViewController {
        let view = Homev2ViewController()
        let interactor = Homev2Interactor()
        let router = Homev2Router(viewController: view)
        let presenter = Homev2Presenter(view: view,
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
