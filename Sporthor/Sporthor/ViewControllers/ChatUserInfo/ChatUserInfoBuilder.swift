//
//  ChatUserInfoBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import Foundation

final class ChatUserInfoBuilder {
    static func build(userId: String, groupId: String) -> ChatUserInfoViewController {
        let view = ChatUserInfoViewController()
        let interactor = ChatUserInfoInteractor()
        let router = ChatUserInfoRouter(viewController: view)
        let presenter = ChatUserInfoPresenter(view: view, interactor: interactor, router: router, userId: userId, groupId: groupId)
        view.presenter = presenter
        return view
    }
}
