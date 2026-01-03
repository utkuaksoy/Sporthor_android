//
//  CalendarDetailPresenter.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation
import CommonKit

final class CalendarDetailPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarDetailPresenterDelegate? {
        get { return self.baseView as? CalendarDetailPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarDetailInteractorProtocol {
        get { return self.baseInteractor as! CalendarDetailInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarDetailRouterProtocol {
        get { return self.baseRouter as! CalendarDetailRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarDetailPresenterDelegate,
         interactor: CalendarDetailInteractorProtocol,
         router: CalendarDetailRouterProtocol,
         selectedDate: Date) {
        self.selectedDate = selectedDate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var selectedDate: Date
    var tasks: [GetCalendarDetailTaskModel] = []
}

// MARK: - CalendarDetailPresenterProtocol
extension CalendarDetailPresenter: CalendarDetailPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.prepareNavigationBar()
        didChangeDate(selectedDate)
    }
    
    private func navigate(_ routes: CalendarDetailRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
    
    func didChangeDate(_ date: Date) {
        selectedDate = date
        view?.didSetTitle(selectedDate.toString("MMMM"))
        view?.setCalendarSelectedDate(selectedDate)
        getCalendarDetail(date.toString())
    }
    
    func getCalendarDetail(_ date: String) {
        Task { @MainActor in
            await interactor.getCalendarDetail(date)
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        guard let model = tasks[safe: indexPath.row], model.isOwn else { return }
        navigate(.editMission(delegate: self, model: model))
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 1:
            navigate(.addMissionTypeSelector(delegate: self))
        default:
            break
        }
    }
    
    func didTappedRPEButton(_ model: GetCalendarDetailTaskModel) {
        navigate(.rpe(model: model, delegate: self))
    }
    
    func didTappedLocationButton(_ model: GetCalendarDetailTaskModel) {
        guard let lat = model.location?.lat,
              let lng = model.location?.lng else {
            showAlert(type: .warning, message: "Konum bilgisi bulunamadı.")
            return
        }
        BaseHelper.shared.openMap(latitude: lat, longitude: lng)
    }
}

// MARK: - CalendarDetailInteractorDelegate
extension CalendarDetailPresenter: CalendarDetailInteractorDelegate {
    func didGetCalendarDetail(_ tasks: [GetCalendarDetailTaskModel]) {
        self.tasks = tasks
        view?.reloadData()
    }
}

// MARK: - CalendarAddMissionTypeSelectorDelegate
extension CalendarDetailPresenter: CalendarAddMissionTypeSelectorDelegate {
    func didTappedCalendarAddMission() {
        navigate(.addMission(delegate: self))
    }
    
    func didTappedCalendarBookmark() {
        navigate(.calendarBookmark(delegate: self))
    }
}

extension CalendarDetailPresenter: CalendarAddMissionDelegate {
    func didCalendarAddMission() {
        getCalendarDetail(selectedDate.toString())
    }
}

extension CalendarDetailPresenter: CalendarRPEDelegate {
    func successRPESurveyCalendarRPE() {
        getCalendarDetail(selectedDate.toString())
    }
}


