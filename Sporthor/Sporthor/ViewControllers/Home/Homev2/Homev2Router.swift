//
//  Homev2Router.swift
//  Sporthor
//
//  Created by derTurke on 25.06.2025.
//
//

import Foundation

final class Homev2Router: BaseRouter {}

// MARK: - Homev2RouterProtocol
extension Homev2Router: Homev2RouterProtocol {
    func handleRouter(_ router: Homev2Routes) {
        switch router {
        case .profile(username: let username, userId: let userId):
            let vc = ProfileBuilder.build(userId: userId, userName: username)
            viewController.show(vc, sender: nil)
        case .addStory(let previewDelegate):
            let vc = CreateStoryBuilder.build(previewDelegate: previewDelegate)
            let nav = CustomNavigationController(rootViewController: vc)
            nav.modalPresentationStyle = .fullScreen
            viewController.present(nav, animated: true, completion: nil)
        case .story(delegate: let delegate, model: let model):
            let vc = StoryBuilder.build(delegate: delegate, model: model)
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: false)
        case .comment(delegate: let delegate, postId: let postId):
            let vc = CommentBuilder.build(postId: postId, delegate: delegate)
            viewController.presentPanModal(vc)
        case .postSetting(delegate: let delegate, model: let model):
            let vc = PostSettingBuilder.build(delegate: delegate, model: model)
            viewController.presentPanModal(vc)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .calendarMain:
            let vc = CalendarMainBuilder.build()
            viewController.show(vc, sender: nil)
        case .menu:
            let vc = MenuBuilder.build()
            let navCon = CustomNavigationController(rootViewController: vc)
            navCon.modalTransitionStyle = .crossDissolve
            navCon.modalPresentationStyle = .fullScreen
            viewController.present(navCon, animated: true)
        case .notification:
            let vc = NotificationBuilder.build()
            viewController.show(vc, sender: nil)
        }
    }
}
