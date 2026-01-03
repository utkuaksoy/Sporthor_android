//
//  AddPersonTrainingGroupRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit

final class AddPersonTrainingGroupRouter: BaseRouter {}

// MARK: - AddPersonTrainingGroupRouterProtocol
extension AddPersonTrainingGroupRouter: AddPersonTrainingGroupRouterProtocol {
    func handleRouter(_ router: AddPersonTrainingGroupRoutes) {
        switch router {
        case .back:
            viewController.navigationController?.popViewController(animated: true)
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
