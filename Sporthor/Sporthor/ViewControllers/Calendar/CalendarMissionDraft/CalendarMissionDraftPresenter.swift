//
//  CalendarMissionDraftPresenter.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//
//

import Foundation

final class CalendarMissionDraftPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarMissionDraftPresenterDelegate? {
        get { return self.baseView as? CalendarMissionDraftPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarMissionDraftInteractorProtocol {
        get { return self.baseInteractor as! CalendarMissionDraftInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarMissionDraftRouterProtocol {
        get { return self.baseRouter as! CalendarMissionDraftRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarMissionDraftPresenterDelegate,
         interactor: CalendarMissionDraftInteractorProtocol,
         router: CalendarMissionDraftRouterProtocol,
         delegate: CalendarAddMissionDelegate?) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.calendarAddMissionDelegate = delegate
    }
    
    private weak var calendarAddMissionDelegate: CalendarAddMissionDelegate?
    var drafts: [GetCalendarDetailTaskModel] = []
}

// MARK: - CalendarMissionDraftPresenterProtocol
extension CalendarMissionDraftPresenter: CalendarMissionDraftPresenterProtocol {
    func viewDidLoad() {
        view?.prepareNavigationBar()
        view?.didSetTitle("Etkinlik Şablonları")
        view?.prepareUI()
        getDrafts()
    }
    
    private func getDrafts() {
        Task { @MainActor in
            await interactor.getDrafts()
        }
    }
    
    private func navigate(_ routes: CalendarMissionDraftRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back(delegate: nil))
        default:
            break
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        navigate(.addMission(delegate: self, model: drafts[indexPath.row]))
    }
}

// MARK: - CalendarMissionDraftInteractorDelegate
extension CalendarMissionDraftPresenter: CalendarMissionDraftInteractorDelegate {
    func didGetDrafts(_ drafts: [GetCalendarDetailTaskModel]) {
        self.drafts = drafts
        view?.reloadData()
    }
}

// MARK: - CalendarAddMissionDelegate
extension CalendarMissionDraftPresenter: CalendarAddMissionDelegate {
    func didCalendarAddMission() {
        navigate(.back(delegate: calendarAddMissionDelegate))
    }
}

