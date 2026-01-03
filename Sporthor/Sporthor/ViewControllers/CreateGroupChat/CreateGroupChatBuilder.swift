//
//  CreateGroupChatBuilder.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import Foundation

enum CreateGroupChatViewType {
    case newGroup
    case addMembers(groupId: String)
    
    static func == (lhs: CreateGroupChatViewType, rhs: CreateGroupChatViewType) -> Bool {
           switch (lhs, rhs) {
           case (.newGroup, .newGroup):
               return true
           case (.addMembers, .addMembers):
               return true 
           default:
               return false
           }
       }
}

final class CreateGroupChatBuilder {
    static func build(with fromViewType: CreateGroupChatViewType) -> CreateGroupChatViewController {
        let view = CreateGroupChatViewController()
        let interactor = CreateGroupChatInteractor()
        let router = CreateGroupChatRouter(viewController: view)
        let presenter = CreateGroupChatPresenter(view: view, interactor: interactor, router: router, fromType: fromViewType)
        view.presenter = presenter
        return view
    }
}
