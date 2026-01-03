//
//  EditPersonAndTechnicalStaffTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 31.10.2025.
//
//

import UIKit
import PanModal
import CommonKit

final class EditPersonAndTechnicalStaffTrainingGroupRouter: BaseRouter {}

// MARK: - EditPersonAndTechnicalStaffTrainingGroupRouterProtocol
extension EditPersonAndTechnicalStaffTrainingGroupRouter: EditPersonAndTechnicalStaffTrainingGroupRouterProtocol {
    func handleRouter(_ router: EditPersonAndTechnicalStaffTrainingGroupRoutes) {
        switch router {
        case .updateTechnicalStaff(delegate: let delegate,
                                   trainingGroupId: let trainingGroupId,
                                   userId: let userId,
                                   image: let image,
                                   name: let name,
                                   role: let role,
                                   index: let index):
            let vc = AddTechnicalStaffBuilder.build(delegate: delegate,
                                                    trainingGroupId: trainingGroupId,
                                                    userId: userId,
                                                    image: image,
                                                    name: name,
                                                    role: role,
                                                    index: index,
                                                    isUpdate: true)
            viewController.presentPanModal(vc)
        case .addPersonTrainingGroup(role: let role,
                                     trainingGroup: let trainingGroup,
                                     isEdit: let isEdit,
                                     users: let users,
                                     coaches: let coaches,
                                     delegate: let delegate,
                                     isUpdateCoach: let isUpdateCoach):
            let vc = AddPersonWithRoleTrainingGroupBuilder.build(role: role,
                                                                 trainingGroup: trainingGroup,
                                                                 isEdit: isEdit,
                                                                 users: users,
                                                                 coaches: coaches,
                                                                 delegate: delegate,
                                                                 isUpdateCoach: isUpdateCoach)
            viewController.show(vc, sender: nil)
        case .backToListTrainingViewController:
            BaseHelper.shared.navigateToBackViewController(ListTrainingGroupViewController.self)
        case .dashboard:
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = scene.windows.first {
                let tabBarController = CustomTabBarController()
                window.rootViewController = tabBarController
                window.backgroundColor = .white
                window.makeKeyAndVisible()
            }
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .myTeamCoaches:
            BaseHelper.shared.navigateToBackViewController(MyTeamCoachesViewController.self)
        }
    }
}
