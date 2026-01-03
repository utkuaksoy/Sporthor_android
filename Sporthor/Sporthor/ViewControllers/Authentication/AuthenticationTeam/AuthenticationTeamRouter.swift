//
//  AuthenticationTeamRouter.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//
//

import Foundation

final class AuthenticationTeamRouter: BaseRouter {}

// MARK: - AuthenticationTeamRouterProtocol
extension AuthenticationTeamRouter: AuthenticationTeamRouterProtocol {
    func handleRouter(_ router: AuthenticationTeamRoutes) {
        switch router {
        case .home:
            let vc = CustomTabBarController()
            vc.modalTransitionStyle = .crossDissolve
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: true)
        case .trainingGroup(let selectedTeams, let isLogin):
            let vc = CreateTrainingGroupBuilder.build(selectedTeams: selectedTeams, isLogin: isLogin)
            viewController.show(vc, sender: nil)
        case .createClub(let isLogin):
            let vc = CreateClubBuilder.build(isLogin: isLogin)
            viewController.show(vc, sender: nil)
        case .sendClubAuthorizationLetter(sportClub: let sportClub):
            let vc = SendClubAuthorizationLetterBuilder.build(sportClub: sportClub)
            viewController.show(vc, sender: nil)
        }
    }
}
