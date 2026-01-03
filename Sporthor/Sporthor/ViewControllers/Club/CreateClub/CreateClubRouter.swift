//
//  CreateClubRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class CreateClubRouter: BaseRouter {}

// MARK: - CreateClubRouterProtocol
extension CreateClubRouter: CreateClubRouterProtocol {
    func handleRouter(_ router: CreateClubRoutes) {
        switch router {
        case .showAlertController(let alertController):
            viewController.present(alertController, animated: true)
        case .camera(let imagePickerController):
            viewController.present(imagePickerController, animated: true)
        case .location(let delegate):
            let vc = LocationBuilder.build(delegate: delegate,
                                           isDarkTheme: false,
                                           isPlacemark: true)
            let nav = CustomNavigationController(rootViewController: vc)
            nav.modalPresentationStyle = .fullScreen
            viewController.present(nav, animated: true)
        case .successCreateClub(let delegate, let sportClub, let infoTitle, let infoDescription):
            let vc = SuccessCreateClubBuilder.build(delegate: delegate,
                                                    sportClub: sportClub,
                                                    infoTitle: infoTitle,
                                                    infoDescription: infoDescription)
            viewController.presentPanModal(vc)
        case .sendClubAuthorizationLetter(let sportClub):
            let vc = SendClubAuthorizationLetterBuilder.build(sportClub: sportClub)
            viewController.show(vc, sender: nil)
        case .home:
            let vc = CustomTabBarController()
            vc.modalTransitionStyle = .crossDissolve
            vc.modalPresentationStyle = .fullScreen
            viewController.present(vc, animated: true)
        case .createTrainingGroup:
            let vc = CreateTrainingGroupBuilder.build()
            viewController.show(vc, sender: nil)
        case .selection(title: let title,
                        model: let model,
                        delegate: let delegate,
                        isSingleSelection: let isSingleSelection):
            let vc = ProfileEditAddBranchBuilder.build(delegate: delegate,
                                                       title: title,
                                                       model: model,
                                                       isSingleSelection: isSingleSelection)
            let navCon = CustomNavigationController(rootViewController: vc)
            navCon.modalPresentationStyle = .fullScreen
            viewController.present(navCon, animated: true)
        }
    }
}
