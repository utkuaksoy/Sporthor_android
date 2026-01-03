//
//  ProfileRouter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//
//

import ChatKit
import ChatCoordinator
import Factory
import UIKit
import PanModal

final class ProfileRouter: BaseRouter {}

// MARK: - ProfileRouterProtocol
extension ProfileRouter: ProfileRouterProtocol {
    func handleRouter(_ router: ProfileRoutes) {
        switch router {
        case .chat(let userId, let userName, let image, let toUserId):
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            Container.shared.chatCoordinator()?.start(
                navigationController: navigationController,
                delegate: self,
                chatPartner: .init(
                    senderId: userId,
                    displayName: userName,
                    image: image,
                    isGroup: false,
                    toUserId: toUserId
                )
            )
        case .settings:
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            let controller = SettingsBuilder.build()
            navigationController.pushViewController(controller, animated: true)
        case .followers(let userId, let userName, let direction, let followersCount, let followingCount, let isCurrentUser):
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            let controller = FollowersBuilder.build(
                userId: userId,
                userName: userName,
                followersCount: followersCount,
                followingCount: followingCount,
                isCurrentUser: isCurrentUser,
                direction: direction
            )
            navigationController.pushViewController(controller, animated: true)
        case .profileEdit(delegate: let delegate):
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            let vc = ProfileEditBuilder.build(delegate: delegate)
            navigationController.pushViewController(vc, animated: true)
        case .postDetail(posts: let posts, profileUserId: let profileUserId, initialPostId: let initialPostId):
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            let vc = Homev2Builder.build(
                homeModeType: .profile,
                posts: posts,
                profileUserId: profileUserId,
                initialPostId: initialPostId
            )
            navigationController.pushViewController(vc, animated: true)
        case .profileSettings(delegate: let delegate, userId: let userId):
            let vc = ProfileSettingBuilder.build(delegate: delegate, userId: userId)
            viewController.presentPanModal(vc)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}

extension ProfileRouter: ChatCoordinatorDelegate {
    func navigateToProfile(userId: String, userName: String, isGroup: Bool, groupId: String?) {
        DispatchQueue.main.async {
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            if isGroup {
                let controller = GroupDetailBuilder.build(groupId: userId, groupName: userName, groupImage: "")
                navigationController.pushViewController(controller, animated: true)
            } else {
                guard let groupId else { return }
                let controller = ChatUserInfoBuilder.build(userId: userId, groupId: groupId)
                navigationController.pushViewController(controller, animated: true)
            }
        }
    }
}
