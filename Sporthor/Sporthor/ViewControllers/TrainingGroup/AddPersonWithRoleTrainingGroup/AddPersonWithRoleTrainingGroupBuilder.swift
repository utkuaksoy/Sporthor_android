//
//  AddPersonWithRoleTrainingGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation

final class AddPersonWithRoleTrainingGroupBuilder {
    static func build(
        role: TrainingGroupPersonRole,
        trainingGroup: TrainingGroupResponse,
        isFirst: Bool = false,
        requestModel: AddPersonWithRoleRequest? = nil,
        isEdit: Bool = false,
        users: [GetTrainingGroupUserModelUser] = [],
        coaches: [GetTrainingGroupUserModelUser] = [],
        delegate: AddPersonWithRoleTrainingGroupDelegate? = nil,
        isUpdateCoach: Bool = false
    ) -> AddPersonWithRoleTrainingGroupViewController {
        let view = AddPersonWithRoleTrainingGroupViewController()
        let interactor = AddPersonWithRoleTrainingGroupInteractor()
        let router = AddPersonWithRoleTrainingGroupRouter(viewController: view)
        let presenter = AddPersonWithRoleTrainingGroupPresenter(
            view: view,
            interactor: interactor,
            router: router,
            role: role,
            trainingGroup: trainingGroup,
            isFirst: isFirst,
            requestModel: requestModel,
            isEdit: isEdit,
            selectedUsers: users,
            selectedCoaches: coaches,
            delegate: delegate,
            isUpdateCoach: isUpdateCoach
        )
        view.presenter = presenter
        return view
    }
}
