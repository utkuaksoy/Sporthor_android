//
//  MyTeamCoachesRouter.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

final class MyTeamCoachesRouter: BaseRouter {}

// MARK: - MyTeamCoachesRouterProtocol
extension MyTeamCoachesRouter: MyTeamCoachesRouterProtocol {
    func handleRouter(_ router: MyTeamCoachesRoutes) {
        switch router {
        case .detail(model: let model):
            let vc = MyTeamCoachesDetailBuilder.build(model)
            viewController.show(vc, sender: nil)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
