//
//  SuccessSendClubAuthorizationLetterRouter.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import UIKit

final class SuccessSendClubAuthorizationLetterRouter: BaseRouter {}

// MARK: - SuccessSendClubAuthorizationLetterRouterProtocol
extension SuccessSendClubAuthorizationLetterRouter: SuccessSendClubAuthorizationLetterRouterProtocol {
    func handleRouter(_ router: SuccessSendClubAuthorizationLetterRoutes) {
        switch router {
        case .home:
            if let scene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
               let window = scene.windows.first {
                let tabBarController = CustomTabBarController()
                window.rootViewController = tabBarController
                window.backgroundColor = .white
                window.makeKeyAndVisible()
            }
        case .createTrainingGroup(let teams):
            let vc = CreateTrainingGroupBuilder.build(selectedTeams: teams)
            viewController.show(vc, sender: nil)
        }
    }
}
