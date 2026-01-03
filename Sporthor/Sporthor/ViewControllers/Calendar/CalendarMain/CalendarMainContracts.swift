//
//  CalendarMainContracts.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation

protocol CalendarMainPresenterProtocol: BasePresenterProtocol {
    var view: CalendarMainPresenterDelegate? { get set }
    var interactor: CalendarMainInteractorProtocol { get set }
    var router: CalendarMainRouterProtocol { get set }
    var calendarEvents: [CalendarEvent] { get set }
    
    func viewDidLoad()
    func didTappedCKButton(_ tag: Int)
    func moveCurrentPage(by months: Int, currentPage: Date?)
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectDate(_ date: Date)
    func didTappedAddMissionButton()
    func calendarCurrentPageDidChange(_ date: Date)
}

protocol CalendarMainPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func setCurrentPage(newPage: Date)
    func didChangeMonthName(_ text: String)
    func calendarReloadData()
}

protocol CalendarMainInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarMainInteractorDelegate? { get set }
    func getCalendar(_ date: String) async
}

protocol CalendarMainInteractorDelegate: BaseInteractorDelegate {
    func didGetCalendar(_ calendarEvents: [CalendarEvent])
}

protocol CalendarMainRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarMainRoutes)
}

enum CalendarMainRoutes {
    case back
    case calendarDetail(date: Date)
    case addMission(delegate: CalendarAddMissionDelegate?)
    case addMissionTypeSelector(delegate: CalendarAddMissionTypeSelectorDelegate?)
    case calendarBookmark(delegate: CalendarAddMissionDelegate?)
}
