//
//  CalendarAddMissionTypeSelectorContracts.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//
//

import Foundation

protocol CalendarAddMissionTypeSelectorPresenterProtocol: BasePresenterProtocol {
    var view: CalendarAddMissionTypeSelectorPresenterDelegate? { get set }
    var interactor: CalendarAddMissionTypeSelectorInteractorProtocol { get set }
    var router: CalendarAddMissionTypeSelectorRouterProtocol { get set }
    
    func viewDidLoad()
    func didTappedButton(tag: Int, indexPath: IndexPath?)
}

protocol CalendarAddMissionTypeSelectorPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
}

protocol CalendarAddMissionTypeSelectorInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarAddMissionTypeSelectorInteractorDelegate? { get set }
}

protocol CalendarAddMissionTypeSelectorInteractorDelegate: BaseInteractorDelegate {
}

protocol CalendarAddMissionTypeSelectorRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarAddMissionTypeSelectorRoutes)
}

enum CalendarAddMissionTypeSelectorRoutes {
    case didBackCalendarAddMission(delegate: CalendarAddMissionTypeSelectorDelegate?)
    case didBackCalendarBookmark(delegate: CalendarAddMissionTypeSelectorDelegate?)
}

protocol CalendarAddMissionTypeSelectorDelegate: AnyObject {
    func didTappedCalendarAddMission()
    func didTappedCalendarBookmark()
}
