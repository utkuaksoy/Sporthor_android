//
//  CalendarRPEContracts.swift
//  Sporthor
//
//  Created by derTurke on 25.07.2025.
//
//

import Foundation

protocol CalendarRPEPresenterProtocol: BasePresenterProtocol {
    var view: CalendarRPEPresenterDelegate? { get set }
    var interactor: CalendarRPEInteractorProtocol { get set }
    var router: CalendarRPERouterProtocol { get set }
    var rpeModel: [RPEModel] { get set }
    
    func viewDidLoad()
    func didSelectStar(at index: Int)
    func didTappedCKButton(_ tag: Int)
}

protocol CalendarRPEPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
    func prepareStar(selectedStar: Int)
}

protocol CalendarRPEInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarRPEInteractorDelegate? { get set }
    func rpeSurvey(_ request: [String: Any]) async
}

protocol CalendarRPEInteractorDelegate: BaseInteractorDelegate {
    func didRPESurvey()
}

protocol CalendarRPERouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarRPERoutes)
}

enum CalendarRPERoutes {
    case rpeSurvey(delegate: CalendarRPEDelegate?)
}

protocol CalendarRPEDelegate: AnyObject {
    func successRPESurveyCalendarRPE()
}
