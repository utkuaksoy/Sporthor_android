//
//  CalendarDetailContracts.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation

protocol CalendarDetailPresenterProtocol: BasePresenterProtocol {
    var view: CalendarDetailPresenterDelegate? { get set }
    var interactor: CalendarDetailInteractorProtocol { get set }
    var router: CalendarDetailRouterProtocol { get set }
    var tasks: [GetCalendarDetailTaskModel] { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didChangeDate(_ date: Date)
    func didSelectRowAt(_ indexPath: IndexPath)
    func didTappedCKButton(_ tag: Int)
    func didTappedRPEButton(_ model: GetCalendarDetailTaskModel)
    func didTappedLocationButton(_ model: GetCalendarDetailTaskModel)
}

protocol CalendarDetailPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func setCalendarSelectedDate(_ date: Date)
    func reloadData()
}

protocol CalendarDetailInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarDetailInteractorDelegate? { get set }
    func getCalendarDetail(_ date: String) async
}

protocol CalendarDetailInteractorDelegate: BaseInteractorDelegate {
    func didGetCalendarDetail(_ tasks: [GetCalendarDetailTaskModel])
}

protocol CalendarDetailRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarDetailRoutes)
}

enum CalendarDetailRoutes {
    case back
    case addMission(delegate: CalendarAddMissionDelegate?)
    case addMissionTypeSelector(delegate: CalendarAddMissionTypeSelectorDelegate?)
    case calendarBookmark(delegate: CalendarAddMissionDelegate?)
    case editMission(delegate: CalendarAddMissionDelegate?,
                     model: GetCalendarDetailTaskModel)
    case rpe(model: GetCalendarDetailTaskModel,
             delegate: CalendarRPEDelegate)
}
