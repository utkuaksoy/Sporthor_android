//
//  CalendarAddMissionInteractor.swift
//  Sporthor
//
//  Created by derTurke on 22.05.2025.
//
//

import Foundation
import Factory

final class CalendarAddMissionInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarAddMissionInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarAddMissionInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarAddMissionInteractorProtocol
extension CalendarAddMissionInteractor: CalendarAddMissionInteractorProtocol {
    func getTaskType() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CalendarService.getTaskType, responseType: EventTypeResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didGetTaskType(response.types)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addTask(_ request: [String: Any], isEdit: Bool) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: isEdit ? CalendarService.updateTask(request) : CalendarService.addTask(request),
            responseType: EventTypeDetailResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didAddTask()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
