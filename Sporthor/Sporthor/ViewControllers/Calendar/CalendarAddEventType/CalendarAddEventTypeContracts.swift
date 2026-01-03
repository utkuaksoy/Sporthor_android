//
//  CalendarAddEventTypeContracts.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

protocol CalendarAddEventTypePresenterProtocol: BasePresenterProtocol {
    var view: CalendarAddEventTypePresenterDelegate? { get set }
    var interactor: CalendarAddEventTypeInteractorProtocol { get set }
    var router: CalendarAddEventTypeRouterProtocol { get set }
    
    func viewDidLoad()
    func didEndEditingTextField(_ text: String)
    func didTappedSubmitButton()
    
}

protocol CalendarAddEventTypePresenterDelegate: BasePresenterDelegate {
    func prepareUI()
}

protocol CalendarAddEventTypeInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarAddEventTypeInteractorDelegate? { get set }
    func addTaskType(_ request: [String: Any]) async
}

protocol CalendarAddEventTypeInteractorDelegate: BaseInteractorDelegate {
    func didAddTaskType(_ model: EventTypeModel)
}

protocol CalendarAddEventTypeRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarAddEventTypeRoutes)
}

enum CalendarAddEventTypeRoutes {
    case dismiss(delegate: CalendarAddEventTypeDelegate?, model: EventTypeModel?)
}

protocol CalendarAddEventTypeDelegate: AnyObject {
    func didAddEventType(_ eventType: EventTypeModel?)
}
