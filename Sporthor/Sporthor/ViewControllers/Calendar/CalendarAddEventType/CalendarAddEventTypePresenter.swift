//
//  CalendarAddEventTypePresenter.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarAddEventTypePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarAddEventTypePresenterDelegate? {
        get { return self.baseView as? CalendarAddEventTypePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarAddEventTypeInteractorProtocol {
        get { return self.baseInteractor as! CalendarAddEventTypeInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarAddEventTypeRouterProtocol {
        get { return self.baseRouter as! CalendarAddEventTypeRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarAddEventTypePresenterDelegate,
         interactor: CalendarAddEventTypeInteractorProtocol,
         router: CalendarAddEventTypeRouterProtocol,
         delegate: CalendarAddEventTypeDelegate?) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.calendarAddEventTypeDelegate = delegate
    }
    
    private weak var calendarAddEventTypeDelegate: CalendarAddEventTypeDelegate?
    private var eventTypeName: String = ""
}

// MARK: - CalendarAddEventTypePresenterProtocol
extension CalendarAddEventTypePresenter: CalendarAddEventTypePresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
    }
    
    private func navigate(_ routes: CalendarAddEventTypeRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didEndEditingTextField(_ text: String) {
        eventTypeName = text
    }
    
    func didTappedSubmitButton() {
        guard let _ = calendarAddEventTypeDelegate,
              !eventTypeName.isEmpty else {
            navigate(.dismiss(delegate: nil, model: nil))
            return
        }
        
        addTaskType()
    }
    
    private func addTaskType() {
        let request: [String: Any] = ["typeName": eventTypeName]
        Task { @MainActor in
            await interactor.addTaskType(request)
        }
    }
}

// MARK: - CalendarAddEventTypeInteractorDelegate
extension CalendarAddEventTypePresenter: CalendarAddEventTypeInteractorDelegate {
    func didAddTaskType(_ model: EventTypeModel) {
        navigate(.dismiss(delegate: calendarAddEventTypeDelegate, model: model))
    }
}
