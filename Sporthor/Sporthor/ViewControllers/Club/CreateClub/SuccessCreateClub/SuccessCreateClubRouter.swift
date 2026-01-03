//
//  SuccessCreateClubRouter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessCreateClubRouter: BaseRouter {}

// MARK: - SuccessCreateClubRouterProtocol
extension SuccessCreateClubRouter: SuccessCreateClubRouterProtocol {
    func handleRouter(_ router: SuccessCreateClubRoutes) {
        switch router {
        case .sendAuthorizationLetter(delegate: let delegate):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didTappedSendAuthorizationLetter()
            }
        case .skip(delegate: let delegate):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.didTappedSkipButton()
            }
        }
    }
}
