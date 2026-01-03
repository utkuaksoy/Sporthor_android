//
//  CalendarAddMissionPresenter.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//
//

import Foundation
import MapKit

final class CalendarAddMissionPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarAddMissionPresenterDelegate? {
        get { return self.baseView as? CalendarAddMissionPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarAddMissionInteractorProtocol {
        get { return self.baseInteractor as! CalendarAddMissionInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarAddMissionRouterProtocol {
        get { return self.baseRouter as! CalendarAddMissionRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarAddMissionPresenterDelegate,
         interactor: CalendarAddMissionInteractorProtocol,
         router: CalendarAddMissionRouterProtocol,
         delegate: CalendarAddMissionDelegate?,
         model: GetCalendarDetailTaskModel?,
         isEdit: Bool,
         isDraftEdit: Bool) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.delegate = delegate
        self.model = model
        self.isEdit = isEdit
        self.isDraftEdit = isDraftEdit
    }
    var headerTitle: String = ""
    var eventTypes: [EventTypeModel] = []
    var startDate: Date = Date()
    var startHour: String = Date().toString("HH:mm")
    var endDate: Date = Date()
    var endHour: String = {
        let calendar = Calendar.current
        if let twoHoursLater = calendar.date(byAdding: .hour, value: 2, to: Date()) {
            return twoHoursLater.toString("HH:mm")
        }
        return Date().toString("HH:mm")
    }()
    
    private var selectedDateType: TitleDateAndHourTableViewCellType = .start
    var descriptionText: String = ""
    var placemark: CLPlacemark?
    var isFullDay: Bool = false
    var isRepeat: Bool = false
    var repeatTaskTime: [RepeatTaskTimeModel] = RepeatTaskTime.allCases.map({ RepeatTaskTimeModel(type: $0, isSelected: false) })
    var profileImages: [String] = []
    private var selectedGroups: [TeamItemModel] = []
    private var persons: [GetTrainingGroupUserModelUser] = []
    var isDraft: Bool = false
    private weak var delegate: CalendarAddMissionDelegate?
    private var model: GetCalendarDetailTaskModel?
    var isEdit: Bool = false
    var isDraftEdit: Bool = false
}

// MARK: - CalendarAddMissionPresenterProtocol
extension CalendarAddMissionPresenter: CalendarAddMissionPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Yeni Ektinlik Oluştur")
        view?.prepareNavigationBar()
        view?.prepareUI()
        view?.didChangeSubmitButtonTitle(isEdit ? "Güncelle" : "Gönder")
        getTaskTypes()
    }
    
    private func getTaskTypes() {
        Task { @MainActor in
            await interactor.getTaskType()
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back(delegate: nil))
        default:
            break
        }
    }
    
    private func navigate(_ routes: CalendarAddMissionRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func textFieldDidEndEditing(_ text: String, tag: Int) {
        self.headerTitle = text
    }
    
    func didSelectEventType(_ eventType: EventTypeModel) {
        eventTypes.enumerated().forEach { index, type in
            eventTypes[index].isSelected = false
        }
        
        if let index = eventTypes.firstIndex(where: { $0.value == eventType.value }) {
            eventTypes[index].isSelected = true
        }
        
        view?.reloadData()
    }
    
    func addEventType() {
        navigate(.addEventType(delegate: self))
    }
    
    func didTappedDate(_ type: TitleDateAndHourTableViewCellType) {
        self.selectedDateType = type
        switch type {
        case .start:
            navigate(.date(selectedDate: startDate, delegate: self))
        case .end:
            navigate(.date(selectedDate: endDate, delegate: self))
        }
    }
    
    func didTappedHour(_ type: TitleDateAndHourTableViewCellType) {
        self.selectedDateType = type
        switch type {
        case .start:
            navigate(.hour(selectedHour: startHour, delegate: self))
        case .end:
            navigate(.hour(selectedHour: endHour, delegate: self))
        }
    }
    
    func didTappedAddDescriptionButton() {
        navigate(.addDescription(delegate: self, description: descriptionText))
    }
    
    func didTappedCommunity() {
        navigate(.addPersonOrGroup(delegate: self,
                                   trainingGroup: selectedGroups,
                                   users: persons))
    }
    
    func didTappedLocationMenu() {
        navigate(.mapView(delegate: self, placemark: placemark))
    }
    
    func getLocationDescription() -> (name: String?, address: String?) {
        guard let placemark else { return (nil, nil) }
        
        let name = placemark.areasOfInterest?.first ?? placemark.name
        
        let addressComponents = [
            placemark.thoroughfare,
            placemark.subThoroughfare,
            placemark.locality,
            placemark.administrativeArea,
            placemark.country
        ]
        
        let address = addressComponents
            .compactMap { $0 }
            .joined(separator: ", ")
        
        return (name, address.isEmpty ? nil : address)
    }
    
    func didChangeSwitch(isOn: Bool, tag: Int) {
        isFullDay = isOn
        view?.reloadData()
    }
    
    func didTappedSelectRepeatTask(tag: Int) {
        for i in repeatTaskTime.indices {
            repeatTaskTime[i].isSelected = false
        }
        if let index = repeatTaskTime.firstIndex(where: { $0.type.rawValue == tag }) {
            repeatTaskTime[index].isSelected = true
        }
        view?.reloadData()
    }
    
    func didChangeSwitchRepeatTask(isOn: Bool, tag: Int) {
        isRepeat = isOn
        for i in repeatTaskTime.indices {
            repeatTaskTime[i].isSelected = false
        }
        view?.reloadData()
    }
    
    func selectedCheckboxTableViewCell(_ isSelected: Bool, tag: Int) {
        isDraft = isSelected
        view?.didChangeSubmitButtonTitle(isSelected ? "Etkinliği Kaydet ve Gönder" : "Gönder")
        view?.reloadData()
    }
    
    func didTappedSubmitButton() {
        if headerTitle.isEmpty {
            showAlert(type: .warning, message: "Etkinlik Başlığı boş olamaz!")
            return
        }
        
        if isRepeat && !repeatTaskTime.contains(where: { $0.isSelected }) {
            showAlert(type: .warning, message: "Etkinlik Tekrarı seçiniz!")
            return
        }
        
        if !eventTypes.contains(where: { $0.isSelected }) {
            showAlert(type: .warning, message: "Etkinlik Tipi seçiniz!")
            return
        }
            
        if descriptionText.isEmpty {
            showAlert(type: .warning, message: "Açıklama boş olamaz!")
            return
        }
        
        
        let addressComponents = [
            placemark?.thoroughfare,
            placemark?.subThoroughfare,
            placemark?.locality,
            placemark?.administrativeArea,
            placemark?.country
        ]
        
        let addressName = placemark?.areasOfInterest?.first ?? placemark?.name
        let address = addressComponents
            .compactMap { $0 }
            .joined(separator: ", ")
        
        var request: [String: Any] = [
            "title": headerTitle,
            "description": descriptionText,
            "startDate": startDate.toString() + " " + startHour,
            "endDate": endDate.toString() + " " + endHour,
            "allDay": isFullDay,
            "location": [
                "title": addressName ?? "",
                "address": address,
                "lat": placemark?.location?.coordinate.latitude ?? 0.0,
                "lng": placemark?.location?.coordinate.longitude ?? 0.0,
            ],
            "isRecurring": isRepeat,
            "isDraft": isDraft
        ]
        
        if !persons.filter({ $0.isSelected }).map({ $0.id }).isEmpty {
            request["userIds"] = persons.filter({ $0.isSelected }).map({ $0.id })
        }
        
        if !selectedGroups.filter({ $0.isSelected }).map({ $0.value }).isEmpty {
            request["trainingGroupIds"] = selectedGroups.filter({ $0.isSelected }).map({ $0.value })
        }
        
        if let taskType: EventTypeModel = eventTypes.filter({ $0.isSelected }).first {
            request["taskType"] = taskType.value
        }
        
        if let repeatTask: RepeatTaskTimeModel = repeatTaskTime.filter({ $0.isSelected }).first {
            request["recurrence"] = repeatTask.type.rawValue
            request["recurrenceEndDate"] = endDate.toString() + " " + endHour
        }
        
        if let taskId = model?.id {
            request["taskId"] = taskId
        }
        
        Task { @MainActor in
            await interactor.addTask(request, isEdit: isEdit)
        }
    }
    
    private func prepareUIWithModel() {
        if let model {
            headerTitle = model.title
            isFullDay = model.allDay
            findLocation(location: model.location)
            isRepeat = model.isRecurring
            if model.isRecurring {
                didTappedSelectRepeatTask(tag: model.recurrence)
            }
            let modelStartDate: Date = model.startDate.toDate(format: "dd/MM/yyyy HH:mm:ss") ?? Date()
            let modelEndDate: Date = model.endDate.toDate(format: "dd/MM/yyyy HH:mm:ss") ?? Date()
            startDate = modelStartDate
            startHour = startDate.toString("HH:mm")
            endDate = modelEndDate
            endHour = endDate.toString("HH:mm")
            
            if let taskType = model.taskType {
                didSelectEventType(taskType)
            }
            
            descriptionText = model.description
            
            if var trainingGroup = model.trainingGroup {
                trainingGroup.isSelected = true
                selectedGroups = [trainingGroup]
            }
            
            var personModel = model.users
            for index in personModel.indices {
                personModel[index].isSelected = true
            }
            persons = personModel
            if !selectedGroups.isEmpty {
                profileImages = selectedGroups.map({ $0.image ?? "" }).suffix(4)
            } else if !persons.isEmpty {
                profileImages = persons.map({ $0.imageUrl }).suffix(4)
            }
            
            view?.reloadData()
        }
    }
        
    private func findLocation(location: GetCalendarDetailLocation?) {
        if let location {
            let cLLocation = CLLocation(latitude: location.lat, longitude: location.lng)
            let geocoder = CLGeocoder()
            geocoder.reverseGeocodeLocation(cLLocation) { [weak self] placemarks, error in
                guard let self else { return }
                if let _ = error {
                    showAlert(type: .warning, message: "Kayıtlı lokasyon bulunamadı")
                    return
                }
                
                guard let placemark = placemarks?.first else {
                    showAlert(type: .warning, message: "Kayıtlı lokasyon bulunamadı")
                    return
                }
                
                self.placemark = placemark
            }
        }
    }
}

// MARK: - CalendarAddMissionInteractorDelegate
extension CalendarAddMissionPresenter: CalendarAddMissionInteractorDelegate {
    func didGetTaskType(_ eventTypes: [EventTypeModel]) {
        self.eventTypes = eventTypes
        prepareUIWithModel()
        view?.reloadData()
    }
    
    func didAddTask() {
        showAlert(delegate: self,
                  type: .success,
                  message: isEdit ? "Etkinlik başarıyla güncellenmiştir." : "Etkinlik başarıyla oluşturulmuştur.",
                  tag: 4)
    }
}

// MARK: - CalendarModalDateDelegate
extension CalendarAddMissionPresenter: CalendarModalDateDelegate {
    func didSelectDate(_ date: Date) {
        switch selectedDateType {
        case .start:
            startDate = date
        case .end:
            endDate = date
        }
        view?.reloadData()
    }
}

// MARK: - CalendarModalHourDelegate
extension CalendarAddMissionPresenter: CalendarModalHourDelegate {
    func didSelectHour(_ hour: String) {
        switch selectedDateType {
        case .start:
            startHour = hour
        case .end:
            endHour = hour
        }
        view?.reloadData()
    }
}

// MARK: - CalendarAddEventTypeDelegate
extension CalendarAddMissionPresenter: CalendarAddEventTypeDelegate {
    func didAddEventType(_ eventType: EventTypeModel?) {
        guard let eventType, !eventType.value.isEmpty else { return }
        eventTypes.append(eventType)
        didSelectEventType(eventType)
    }
}

// MARK: - CalendarAddDescriptionDelegate
extension CalendarAddMissionPresenter: CalendarAddDescriptionDelegate {
    func didAddDescription(_ description: String) {
        self.descriptionText = description
        view?.reloadData()
    }
}

// MARK: - CalendarMapViewDelegate
extension CalendarAddMissionPresenter: CalendarMapViewDelegate {
    func didSelectMapViewLocation(_ placemark: CLPlacemark) {
        self.placemark = placemark
        view?.reloadData()
    }
}

// MARK: - CalendarAddPersonOrGroupDelegate
extension CalendarAddMissionPresenter: CalendarAddPersonOrGroupDelegate {
    func didSelectCalendarAddPersonOrGroup(persons: [GetTrainingGroupUserModelUser],
                                           groups: [TeamItemModel]) {
        self.selectedGroups = groups
        self.persons = persons
        if !groups.isEmpty {
            profileImages = groups.map({ $0.image ?? "" }).suffix(4)
        } else if !persons.isEmpty {
            profileImages = persons.map({ $0.imageUrl }).suffix(4)
        }
        view?.reloadData()
    }
}

extension CalendarAddMissionPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        switch tag {
        case 4:
            navigate(.back(delegate: delegate))
        default:
            break
        }
    }
}
