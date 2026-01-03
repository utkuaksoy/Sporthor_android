//
//  SearchRouter.swift
//  Sporthor
//
//  Created by derTurke on 7.03.2025.
//
//

import Foundation

final class SearchRouter: BaseRouter {}

// MARK: - SearchRouterProtocol
extension SearchRouter: SearchRouterProtocol {
    func handleRouter(_ router: SearchRoutes) {
        switch router {
        case .profile(id: let id, username: let username):
            let vc = ProfileBuilder.build(userId: id, userName: username)
            viewController.show(vc, sender: nil)
        }
    }
}
