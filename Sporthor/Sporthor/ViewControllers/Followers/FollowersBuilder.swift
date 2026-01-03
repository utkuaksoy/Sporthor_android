//
//  FollowersBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Foundation

final class FollowersBuilder {
    static func build(
        userId: String,
        userName: String,
        followersCount: Int,
        followingCount: Int,
        isCurrentUser: Bool,
        direction: FollowDirectionEnum
    ) -> FollowersViewController {
        let view = FollowersViewController()
        let interactor = FollowersInteractor(direction: direction)
        let router = FollowersRouter(viewController: view)
        let presenter = FollowersPresenter(
            view: view,
            interactor: interactor,
            router: router,
            direction: direction,
            userId: userId,
            userName: userName,
            followersCount: followersCount,
            followingCount: followingCount, isCurrentUser: isCurrentUser
        )
        view.presenter = presenter
        return view
    }
}
