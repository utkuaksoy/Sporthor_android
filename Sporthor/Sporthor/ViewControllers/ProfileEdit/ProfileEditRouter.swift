//
//  ProfileEditRouter.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//
//

import Foundation

final class ProfileEditRouter: BaseRouter {}

// MARK: - ProfileEditRouterProtocol
extension ProfileEditRouter: ProfileEditRouterProtocol {
    func handleRouter(_ router: ProfileEditRoutes) {
        switch router {
        case .selection(title: let title,
                        model: let model,
                        delegate: let delegate):
            let vc = ProfileEditAddBranchBuilder.build(delegate: delegate,
                                                       title: title,
                                                       model: model)
            let navCon = CustomNavigationController(rootViewController: vc)
            navCon.modalPresentationStyle = .fullScreen
            viewController.present(navCon, animated: true)
        case .back:
            viewController.navigationController?.popViewController(animated: true)
        case .updateRole:
            let vc = ExperienceJobBuilder.build(isEdit: true)
            viewController.show(vc, sender: nil)
        }
    }
}
