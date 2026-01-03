//
//  CalendarAddPersonOrGroupContracts.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

protocol CalendarAddPersonOrGroupPresenterProtocol: BasePresenterProtocol {
    var view: CalendarAddPersonOrGroupPresenterDelegate? { get set }
    var interactor: CalendarAddPersonOrGroupInteractorProtocol { get set }
    var router: CalendarAddPersonOrGroupRouterProtocol { get set }
    var groups: [TeamItemModel] { get set }
    var filteredUsers: [GetTrainingGroupUserModelUser] { get set }
    
    func viewDidLoad()
    func didSelectRowAt(_ indexPath: IndexPath)
    func selectedGroup(_ model: TeamItemModel)
    func searchBarTextDidChange(_ text: String)
    func didTappedSubmitButton()
}

protocol CalendarAddPersonOrGroupPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
}

protocol CalendarAddPersonOrGroupInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarAddPersonOrGroupInteractorDelegate? { get set }
    
    func getTrainingGroupUser() async
}

protocol CalendarAddPersonOrGroupInteractorDelegate: BaseInteractorDelegate {
    func didGetTrainingGroupUser(_ response: [GetTrainingGroupUserModel])
}

protocol CalendarAddPersonOrGroupRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarAddPersonOrGroupRoutes)
}

enum CalendarAddPersonOrGroupRoutes {
    case dismiss(delegate: CalendarAddPersonOrGroupDelegate?,
                 persons: [GetTrainingGroupUserModelUser],
                 groups: [TeamItemModel])
}

protocol CalendarAddPersonOrGroupDelegate: AnyObject {
    func didSelectCalendarAddPersonOrGroup(persons: [GetTrainingGroupUserModelUser],
                                           groups: [TeamItemModel])
}
