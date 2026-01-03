//
//  GroupDetailBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import Foundation

final class GroupDetailBuilder {
    static func build(
        groupId: String,
        groupName: String,
        groupImage: String
    ) -> GroupDetailViewController {
        let view = GroupDetailViewController()
        let interactor = GroupDetailInteractor(groupId: groupId)
        let router = GroupDetailRouter(viewController: view)
        let presenter = GroupDetailPresenter(
            view: view,
            interactor: interactor,
            router: router,
            groupId: groupId,
            groupName: groupName,
            groupImage: groupImage
        )
        view.presenter = presenter
        return view
    }
} 
