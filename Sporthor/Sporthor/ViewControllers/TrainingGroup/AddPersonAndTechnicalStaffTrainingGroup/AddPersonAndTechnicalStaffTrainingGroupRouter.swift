//
//  AddPersonAndTechnicalStaffTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 27.10.2025.
//
//

import UIKit

final class AddPersonAndTechnicalStaffTrainingGroupRouter: BaseRouter {}

// MARK: - AddPersonAndTechnicalStaffTrainingGroupRouterProtocol
extension AddPersonAndTechnicalStaffTrainingGroupRouter: AddPersonAndTechnicalStaffTrainingGroupRouterProtocol {
    func handleRouter(_ router: AddPersonAndTechnicalStaffTrainingGroupRoutes) {
        switch router {
        case .addPersonWithRole(let role, trainingGroup: let trainingGroup):
            let vc = AddPersonWithRoleTrainingGroupBuilder.build(role: role,
                                                                 trainingGroup: trainingGroup,
                                                                 isFirst: true)
            viewController.show(vc, sender: nil)
            
        case .dashboard:
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = scene.windows.first {
                let tabBarController = CustomTabBarController()
                window.rootViewController = tabBarController
                window.backgroundColor = .white
                window.makeKeyAndVisible()
            }
        }
    }
}
