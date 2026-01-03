//
//  CalendarAddMissionTypeSelectorPresenter.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//
//

import Foundation

final class CalendarAddMissionTypeSelectorPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarAddMissionTypeSelectorPresenterDelegate? {
        get { return self.baseView as? CalendarAddMissionTypeSelectorPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarAddMissionTypeSelectorInteractorProtocol {
        get { return self.baseInteractor as! CalendarAddMissionTypeSelectorInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarAddMissionTypeSelectorRouterProtocol {
        get { return self.baseRouter as! CalendarAddMissionTypeSelectorRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarAddMissionTypeSelectorPresenterDelegate,
         interactor: CalendarAddMissionTypeSelectorInteractorProtocol,
         router: CalendarAddMissionTypeSelectorRouterProtocol, delegate: CalendarAddMissionTypeSelectorDelegate?) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.delegate = delegate
    }
    
    private weak var delegate: CalendarAddMissionTypeSelectorDelegate?
}

// MARK: - CalendarAddMissionTypeSelectorPresenterProtocol
extension CalendarAddMissionTypeSelectorPresenter: CalendarAddMissionTypeSelectorPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
    }
    
    private func navigate(_ routes: CalendarAddMissionTypeSelectorRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedButton(tag: Int, indexPath: IndexPath?) {
        switch tag {
        case 0:
            navigate(.didBackCalendarAddMission(delegate: delegate))
        case 1:
            navigate(.didBackCalendarBookmark(delegate: delegate))
        default:
            break
        }
    }
}

// MARK: - CalendarAddMissionTypeSelectorInteractorDelegate
extension CalendarAddMissionTypeSelectorPresenter: CalendarAddMissionTypeSelectorInteractorDelegate {

}
