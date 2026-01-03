//
//  MenuRouter.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

final class MenuRouter: BaseRouter {}

// MARK: - MenuRouterProtocol
extension MenuRouter: MenuRouterProtocol {
    func handleRouter(_ router: MenuRoutes) {
        switch router {
        case .back:
            viewController.dismiss(animated: true)
        case .webView(let title, let url):
            let vc = WebViewBuilder.build(title: title, url: url)
            viewController.show(vc, sender: nil)
        case .createClub:
            let vc = AuthenticationTeamBuilder.build(isLogin: true, isManager: true)
            viewController.show(vc, sender: nil)
        case .updateClub:
            let vc = ListClubBuilder.build()
            viewController.show(vc, sender: nil)
        case .trainingGroup:
            let vc = AuthenticationTeamBuilder.build(isLogin: true, isCoach: true)
            viewController.show(vc, sender: nil)
        case .trainingGroupList:
            let vc = ListTrainingGroupBuilder.build()
            viewController.show(vc, sender: nil)
        case .coachList:
            let vc = MyTeamCoachesBuilder.build()
            viewController.show(vc, sender: nil)
        case .openSubMenu(menu: let menu, title: let title):
            let vc = MenuBuilder.build(menu: menu, isSubMenu: true, title: title)
            viewController.show(vc, sender: nil)
        case .navigationBack:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
