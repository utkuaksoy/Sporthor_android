//
//  CalendarMainPresenter.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation
import CommonKit

final class CalendarMainPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarMainPresenterDelegate? {
        get { return self.baseView as? CalendarMainPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarMainInteractorProtocol {
        get { return self.baseInteractor as! CalendarMainInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarMainRouterProtocol {
        get { return self.baseRouter as! CalendarMainRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarMainPresenterDelegate,
         interactor: CalendarMainInteractorProtocol,
         router: CalendarMainRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var currentPage: Date?
    var calendarEvents: [CalendarEvent] = []
}

// MARK: - CalendarMainPresenterProtocol
extension CalendarMainPresenter: CalendarMainPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Takvim")
        view?.prepareUI()
    }
    
    private func navigate(_ routes: CalendarMainRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func getCalendar() {
        guard let currentPage else { return }
        Task { @MainActor in
            await interactor.getCalendar(currentPage.toString())
        }
    }
    
    func didTappedCKButton(_ tag: Int) {
        
    }
    
    func moveCurrentPage(by months: Int,
                         currentPage: Date?) {
        guard let currentPage = currentPage,
              let newPage = Calendar.current.date(byAdding: .month, value: months, to: currentPage) else { return }
        self.currentPage = newPage
        getCalendar()
    }
    
    private func changeMonthName(page: Date) {
        view?.didChangeMonthName(page.toString("MMMM"))
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
    
    func didSelectDate(_ date: Date) {
        navigate(.calendarDetail(date: date))
    }
    
    func didTappedAddMissionButton() {
        navigate(.addMissionTypeSelector(delegate: self))
    }
    
    func calendarCurrentPageDidChange(_ date: Date) {
        moveCurrentPage(by: 0, currentPage: date)
    }
        
}

// MARK: - CalendarMainInteractorDelegate
extension CalendarMainPresenter: CalendarMainInteractorDelegate {
    func didGetCalendar(_ calendarEvents: [CalendarEvent]) {
        self.calendarEvents = calendarEvents
        view?.calendarReloadData()
        guard let currentPage else { return }
        view?.setCurrentPage(newPage: currentPage)
        changeMonthName(page: currentPage)
    }
}

// MARK: - CalendarAddMissionDelegate
extension CalendarMainPresenter: CalendarAddMissionDelegate {
    func didCalendarAddMission() {
        getCalendar()
    }
}

extension CalendarMainPresenter: CalendarAddMissionTypeSelectorDelegate {
    func didTappedCalendarAddMission() {
        navigate(.addMission(delegate: self))
    }
    
    func didTappedCalendarBookmark() {
        navigate(.calendarBookmark(delegate: self))
    }
}
