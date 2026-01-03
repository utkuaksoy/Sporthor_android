//
//  CalendarModalHourPresenter.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation

final class CalendarModalHourPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarModalHourPresenterDelegate? {
        get { return self.baseView as? CalendarModalHourPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarModalHourInteractorProtocol {
        get { return self.baseInteractor as! CalendarModalHourInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarModalHourRouterProtocol {
        get { return self.baseRouter as! CalendarModalHourRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarModalHourPresenterDelegate,
         interactor: CalendarModalHourInteractorProtocol,
         router: CalendarModalHourRouterProtocol,
         delegate: CalendarModalHourDelegate? = nil,
         selectedHour: String?) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.calendarModalHourDelegate = delegate
        self.selectedHour = selectedHour
    }
    
    private weak var calendarModalHourDelegate: CalendarModalHourDelegate?
    private var selectedHour: String?
}

// MARK: - CalendarModalHourPresenterProtocol
extension CalendarModalHourPresenter: CalendarModalHourPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetHour(selectedHour?.toDate("HH:mm") ?? Date())
    }
    
    private func navigate(_ routes: CalendarModalHourRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func timeChanged(_ time: Date) {
        selectedHour = time.toString("HH:mm")
    }
    
    func didTappedSubmitButton() {
        guard let selectedHour = selectedHour, !selectedHour.isEmpty else {
            showAlert(type: .warning, message: "Lütfen saat seçiniz.")
            return
        }
        navigate(.dismiss(delegate: calendarModalHourDelegate, selectedHour: selectedHour))
    }
}

// MARK: - CalendarModalHourInteractorDelegate
extension CalendarModalHourPresenter: CalendarModalHourInteractorDelegate {

}
