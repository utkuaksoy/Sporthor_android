//
//  EditGroupNameBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import Foundation

final class EditGroupNameBuilder {
    static func build(groupId: String) -> EditGroupNameViewController {
        let view = EditGroupNameViewController()
        let interactor = EditGroupNameInteractor()
        let router = EditGroupNameRouter(viewController: view)
        let presenter = EditGroupNamePresenter(
            view: view,
            interactor: interactor,
            router: router,
            groupId: groupId
        )
        view.presenter = presenter
        return view
    }
}
