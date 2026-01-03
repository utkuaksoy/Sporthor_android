//
//  ChatMediaAndDocumentBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import Foundation

final class ChatMediaAndDocumentBuilder {
    static func build(groupId: String) -> ChatMediaAndDocumentViewController {
        let view = ChatMediaAndDocumentViewController()
        let interactor = ChatMediaAndDocumentInteractor(groupId: groupId)
        let router = ChatMediaAndDocumentRouter(viewController: view)
        let presenter = ChatMediaAndDocumentPresenter(view: view, interactor: interactor, router: router)
        view.presenter = presenter
        return view
    }
}
