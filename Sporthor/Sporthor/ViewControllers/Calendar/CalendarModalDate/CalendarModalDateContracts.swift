//
//  CalendarModalDateContracts.swift
//  Sporthor
//
//  Created by derTurke on 27.05.2025.
//
//

import Foundation

protocol CalendarModalDatePresenterProtocol: BasePresenterProtocol {
    var view: CalendarModalDatePresenterDelegate? { get set }
    var interactor: CalendarModalDateInteractorProtocol { get set }
    var router: CalendarModalDateRouterProtocol { get set }
    var selectedDate: Date? { get set }
    
    func viewDidLoad()
    func moveCurrentPage(by months: Int, currentPage: Date?)
    func didSelectDate(_ date: Date)
    func didTappedSubmitButton()
}

protocol CalendarModalDatePresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func setCurrentPage(newPage: Date)
    func didChangeMonthName(_ text: String)
    func calendarReloadData()
}

protocol CalendarModalDateInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarModalDateInteractorDelegate? { get set }
}

protocol CalendarModalDateInteractorDelegate: BaseInteractorDelegate {
}

protocol CalendarModalDateRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarModalDateRoutes)
}

enum CalendarModalDateRoutes {
    case dismiss(delegate: CalendarModalDateDelegate?,
                 date: Date)
}

protocol CalendarModalDateDelegate: AnyObject {
    func didSelectDate(_ date: Date)
}
    
