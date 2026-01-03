//
//  CalendarAddMissionContracts.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//
//

import Foundation
import MapKit

protocol CalendarAddMissionPresenterProtocol: BasePresenterProtocol {
    var view: CalendarAddMissionPresenterDelegate? { get set }
    var interactor: CalendarAddMissionInteractorProtocol { get set }
    var router: CalendarAddMissionRouterProtocol { get set }
    var headerTitle: String { get set }
    var eventTypes: [EventTypeModel] { get set }
    var startDate: Date { get set }
    var startHour: String { get set }
    var endDate: Date { get set }
    var endHour: String { get set }
    var descriptionText: String { get set }
    var placemark: CLPlacemark? { get set }
    var isFullDay: Bool { get set }
    var isRepeat: Bool { get set }
    var repeatTaskTime: [RepeatTaskTimeModel] { get set }
    var profileImages: [String] { get set }
    var isDraft: Bool { get set }
    var isEdit: Bool { get set }
    var isDraftEdit: Bool { get set }
    
    func viewDidLoad()
    func didTappedNavigationButton(_ type: BarButtonItemType)
    func textFieldDidEndEditing(_ text: String, tag: Int)
    func didSelectEventType(_ eventType: EventTypeModel)
    func addEventType()
    func didTappedDate(_ type: TitleDateAndHourTableViewCellType)
    func didTappedHour(_ type: TitleDateAndHourTableViewCellType)
    func didTappedAddDescriptionButton()
    func didTappedCommunity()
    func didTappedLocationMenu()
    func getLocationDescription() -> (name: String?, address: String?)
    func didChangeSwitch(isOn: Bool, tag: Int)
    func didTappedSelectRepeatTask(tag: Int)
    func didChangeSwitchRepeatTask(isOn: Bool, tag: Int)
    func selectedCheckboxTableViewCell(_ isSelected: Bool, tag: Int)
    func didTappedSubmitButton()
}

protocol CalendarAddMissionPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
    func didChangeSubmitButtonTitle(_ title: String)
}

protocol CalendarAddMissionInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarAddMissionInteractorDelegate? { get set }
    func getTaskType() async
    func addTask(_ request: [String: Any], isEdit: Bool) async
}

protocol CalendarAddMissionInteractorDelegate: BaseInteractorDelegate {
    func didGetTaskType(_ eventTypes: [EventTypeModel])
    func didAddTask()
}

protocol CalendarAddMissionRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarAddMissionRoutes)
}

enum CalendarAddMissionRoutes {
    case back(delegate: CalendarAddMissionDelegate?)
    case date(selectedDate: Date,
              delegate: CalendarModalDateDelegate?)
    case hour(selectedHour: String,
              delegate: CalendarModalHourDelegate?)
    case addEventType(delegate: CalendarAddEventTypeDelegate?)
    case addDescription(delegate: CalendarAddDescriptionDelegate?,
                        description: String)
    case addPersonOrGroup(delegate: CalendarAddPersonOrGroupDelegate,
                          trainingGroup: [TeamItemModel],
                          users: [GetTrainingGroupUserModelUser])
    case mapView(delegate: CalendarMapViewDelegate?,
                 placemark: CLPlacemark?)
}

protocol CalendarAddMissionDelegate: AnyObject {
    func didCalendarAddMission()
}
