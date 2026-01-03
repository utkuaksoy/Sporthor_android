//
//  AddTechnicalStaffBuilder.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

final class AddTechnicalStaffBuilder {
    static func build(delegate: AddTechnicalStaffDelegate? = nil,
                      trainingGroupId: String = "",
                      userId: String = "",
                      image: String,
                      name: String,
                      role: String,
                      index: Int = 0,
                      isUpdate: Bool = false) -> AddTechnicalStaffViewController {
        let view = AddTechnicalStaffViewController()
        let interactor = AddTechnicalStaffInteractor()
        let router = AddTechnicalStaffRouter(viewController: view)
        let presenter = AddTechnicalStaffPresenter(
            view: view,
            interactor: interactor,
            router: router,
            delegate: delegate,
            trainingGroupId: trainingGroupId,
            userId: userId,
            image: image,
            name: name,
            role: role,
            index: index,
            isUpdate: isUpdate)
        view.presenter = presenter
        return view
    }
}
