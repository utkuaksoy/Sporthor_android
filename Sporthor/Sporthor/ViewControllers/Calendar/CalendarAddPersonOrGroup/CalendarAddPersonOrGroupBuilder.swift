//
//  CalendarAddPersonOrGroupBuilder.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddPersonOrGroupBuilder {
    static func build(delegate: CalendarAddPersonOrGroupDelegate? = nil,
                      trainingGroup: [TeamItemModel] = [],
                      users: [GetTrainingGroupUserModelUser] = []) -> CalendarAddPersonOrGroupViewController {
        let view = CalendarAddPersonOrGroupViewController()
        let interactor = CalendarAddPersonOrGroupInteractor()
        let router = CalendarAddPersonOrGroupRouter(viewController: view)
        let presenter = CalendarAddPersonOrGroupPresenter(view: view,
                                                          interactor: interactor,
                                                          router: router,
                                                          delegate: delegate,
                                                          selectedGroups: trainingGroup,
                                                          selectedUsers: users)
        view.presenter = presenter
        return view
    }
}
