//
//  CalendarMissionDraftContracts.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//
//

import Foundation

protocol CalendarMissionDraftPresenterProtocol: BasePresenterProtocol {
    var view: CalendarMissionDraftPresenterDelegate? { get set }
    var interactor: CalendarMissionDraftInteractorProtocol { get set }
    var router: CalendarMissionDraftRouterProtocol { get set }
    var drafts: [GetCalendarDetailTaskModel] { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func didSelectRowAt(_ indexPath: IndexPath)
}

protocol CalendarMissionDraftPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
}

protocol CalendarMissionDraftInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarMissionDraftInteractorDelegate? { get set }
    func getDrafts() async
}

protocol CalendarMissionDraftInteractorDelegate: BaseInteractorDelegate {
    func didGetDrafts(_ drafts: [GetCalendarDetailTaskModel])
}

protocol CalendarMissionDraftRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarMissionDraftRoutes)
}

enum CalendarMissionDraftRoutes {
    case back(delegate: CalendarAddMissionDelegate?)
    case addMission(delegate: CalendarAddMissionDelegate?,
                    model: GetCalendarDetailTaskModel)
}
