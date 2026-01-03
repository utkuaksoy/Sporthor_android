//
//  AddTechnicalStaffRouter.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

final class AddTechnicalStaffRouter: BaseRouter {}

// MARK: - AddTechnicalStaffRouterProtocol
extension AddTechnicalStaffRouter: AddTechnicalStaffRouterProtocol {
    func handleRouter(_ router: AddTechnicalStaffRoutes) {
        switch router {
        case .addTechnicalStaffSubmit(let delegate,
                                      let role,
                                      let index):
            viewController.dismiss(animated: true) { [weak self] in
                guard let _ = self else { return }
                delegate?.changeRoleAddTechnicalStaff(at: index, to: role)
            }
        }
    }
}
