//
//  CalendarModalDatePresenter.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//
//

import Foundation

final class CalendarModalDatePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarModalDatePresenterDelegate? {
        get { return self.baseView as? CalendarModalDatePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarModalDateInteractorProtocol {
        get { return self.baseInteractor as! CalendarModalDateInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarModalDateRouterProtocol {
        get { return self.baseRouter as! CalendarModalDateRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarModalDatePresenterDelegate,
         interactor: CalendarModalDateInteractorProtocol,
         router: CalendarModalDateRouterProtocol,
         selectedDate: Date?,
         delegate: CalendarModalDateDelegate? = nil) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.selectedDate = selectedDate
        self.calendarModalDateDelegate = delegate
    }
    
    var selectedDate: Date?
    private var calendarModalDateDelegate: CalendarModalDateDelegate?
}

// MARK: - CalendarModalDatePresenterProtocol
extension CalendarModalDatePresenter: CalendarModalDatePresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        moveCurrentPage(by: 0, currentPage: selectedDate)
    }
    
    private func navigate(_ routes: CalendarModalDateRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func moveCurrentPage(by months: Int,
                         currentPage: Date?) {
        guard let currentPage = currentPage,
              let newPage = Calendar.current.date(byAdding: .month, value: months, to: currentPage) else { return }
        view?.setCurrentPage(newPage: newPage)
        changeMonthName(page: newPage)
    }
    
    private func changeMonthName(page: Date) {
        view?.didChangeMonthName(page.toString("MMMM yyyy"))
    }
    
    func didSelectDate(_ date: Date) {
        selectedDate = date
    }
    
    func didTappedSubmitButton() {
        guard let selectedDate else {
            showAlert(type: .warning, message: "Lütfen bir tarih seçin.")
            return
        }
        navigate(.dismiss(delegate: calendarModalDateDelegate, date: selectedDate))
    }
}

// MARK: - CalendarModalDateInteractorDelegate
extension CalendarModalDatePresenter: CalendarModalDateInteractorDelegate {

}
