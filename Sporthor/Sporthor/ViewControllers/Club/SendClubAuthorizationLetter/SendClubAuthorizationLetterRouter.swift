//
//  SendClubAuthorizationLetterRouter.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

final class SendClubAuthorizationLetterRouter: BaseRouter {}

// MARK: - SendClubAuthorizationLetterRouterProtocol
extension SendClubAuthorizationLetterRouter: SendClubAuthorizationLetterRouterProtocol {
    func handleRouter(_ router: SendClubAuthorizationLetterRoutes) {
        switch router {
        case .successSendClubAuthorizationLetter(let sportClub):
            let vc = SuccessSendClubAuthorizationLetterBuilder.build(sportClub: sportClub)
            viewController.show(vc, sender: nil)
        case .home:
            let vc = CustomTabBarController()
            vc.modalTransitionStyle = .crossDissolve
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: true)
        case .createTrainingGroup(let teams):
            let vc = CreateTrainingGroupBuilder.build(selectedTeams: teams)
            viewController.show(vc, sender: nil)
        case .webView(title: let title, url: let url):
            let vc = WebViewBuilder.build(title: title, url: url)
            viewController.show(vc, sender: nil)
        }
    }
}
