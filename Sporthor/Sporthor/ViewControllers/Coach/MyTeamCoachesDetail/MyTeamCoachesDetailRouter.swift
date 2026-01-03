//
//  MyTeamCoachesDetailRouter.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

final class MyTeamCoachesDetailRouter: BaseRouter {}

// MARK: - MyTeamCoachesDetailRouterProtocol
extension MyTeamCoachesDetailRouter: MyTeamCoachesDetailRouterProtocol {
    func handleRouter(_ router: MyTeamCoachesDetailRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .editUser(let trainingGroupResponse, let model, let isUpdateCoach):
            let vc = EditPersonAndTechnicalStaffTrainingGroupBuilder.build(model: model,
                                                                           trainingGroup: trainingGroupResponse,
                                                                           viewType: .technicalStaff,
                                                                           isUpdateCoach: isUpdateCoach)
            viewController.show(vc, sender: nil)
        }
    }
}
