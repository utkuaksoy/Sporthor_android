//
//  AddPersonWithRoleTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import UIKit
import PanModal

final class AddPersonWithRoleTrainingGroupRouter: BaseRouter {}

// MARK: - AddPersonWithRoleTrainingGroupRouterProtocol
extension AddPersonWithRoleTrainingGroupRouter: AddPersonWithRoleTrainingGroupRouterProtocol {
    func handleRouter(_ router: AddPersonWithRoleTrainingGroupRoutes) {
        switch router {
        case .addPerson(role: let role, trainingGroup: let trainingGroup, let requestModel):
            let vc = AddPersonWithRoleTrainingGroupBuilder.build(role: role,
                                                                 trainingGroup: trainingGroup,
                                                                 requestModel: requestModel)
            viewController.show(vc, sender: nil)
        case .addTechnicalStaff(delegate: let delegate,
                                index: let index,
                                image: let image,
                                name: let name,
                                role: let role):
            let vc = AddTechnicalStaffBuilder.build(delegate: delegate,
                                                    image: image,
                                                    name: name,
                                                    role: role,
                                                    index: index)
            viewController.presentPanModal(vc)
        case .dashboard:
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = scene.windows.first {
                let tabBarController = CustomTabBarController()
                window.rootViewController = tabBarController
                window.backgroundColor = .white
                window.makeKeyAndVisible()
            }
            
        case .back(delegate: let delegate, type: let type, users: let users):
            delegate?.didAddPersons(type: type, users: users)
            viewController.navigationController?.popViewController(animated: true)
        }
    }
}
