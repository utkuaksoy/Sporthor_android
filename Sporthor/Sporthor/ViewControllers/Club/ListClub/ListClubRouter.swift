//
//  ListClubRouter.swift
//  Sporthor
//
//  Created by derTurke on 3.07.2025.
//
//

import Foundation

final class ListClubRouter: BaseRouter {}

// MARK: - ListClubRouterProtocol
extension ListClubRouter: ListClubRouterProtocol {
    func handleRouter(_ router: ListClubRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .editSportClub(sportClub: let sportClub):
            let vc = CreateClubBuilder.build(sportClub, isLogin: true)
            viewController.show(vc, sender: nil)
        }
    }
}
