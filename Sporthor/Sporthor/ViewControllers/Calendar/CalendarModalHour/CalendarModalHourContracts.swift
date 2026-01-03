//
//  CalendarModalHourContracts.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

protocol CalendarModalHourPresenterProtocol: BasePresenterProtocol {
    var view: CalendarModalHourPresenterDelegate? { get set }
    var interactor: CalendarModalHourInteractorProtocol { get set }
    var router: CalendarModalHourRouterProtocol { get set }
    
    func viewDidLoad()
    func timeChanged(_ time: Date)
    func didTappedSubmitButton()
}

protocol CalendarModalHourPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetHour(_ date: Date)
}

protocol CalendarModalHourInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarModalHourInteractorDelegate? { get set }
}

protocol CalendarModalHourInteractorDelegate: BaseInteractorDelegate {
}

protocol CalendarModalHourRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarModalHourRoutes)
}

enum CalendarModalHourRoutes {
    case dismiss(delegate: CalendarModalHourDelegate?, selectedHour: String)
}

protocol CalendarModalHourDelegate: AnyObject {
    func didSelectHour(_ hour: String)
}
