//
//  CalendarAddDescriptionPresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation

final class CalendarAddDescriptionPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarAddDescriptionPresenterDelegate? {
        get { return self.baseView as? CalendarAddDescriptionPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarAddDescriptionInteractorProtocol {
        get { return self.baseInteractor as! CalendarAddDescriptionInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarAddDescriptionRouterProtocol {
        get { return self.baseRouter as! CalendarAddDescriptionRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarAddDescriptionPresenterDelegate,
         interactor: CalendarAddDescriptionInteractorProtocol,
         router: CalendarAddDescriptionRouterProtocol,
         delegate: CalendarAddDescriptionDelegate? = nil,
         descriptionText: String) {
        self.descriptionText = descriptionText
        self.calendarAddDescriptionDelegate = delegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private weak var calendarAddDescriptionDelegate: CalendarAddDescriptionDelegate?
    private var descriptionText: String
        
}

// MARK: - CalendarAddDescriptionPresenterProtocol
extension CalendarAddDescriptionPresenter: CalendarAddDescriptionPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.didSetDescription(descriptionText)
    }
    
    private func navigate(_ routes: CalendarAddDescriptionRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didChangeDescription(_ description: String) {
        descriptionText = description
    }
    
    func didTappedButton(_ tag: Int) {
        navigate(.dismiss(delegate: calendarAddDescriptionDelegate, description: descriptionText))
    }
}

// MARK: - CalendarAddDescriptionInteractorDelegate
extension CalendarAddDescriptionPresenter: CalendarAddDescriptionInteractorDelegate {

}
