//
//  CalendarAddEventTypeInteractor.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import Foundation
import Factory

final class CalendarAddEventTypeInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarAddEventTypeInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarAddEventTypeInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarAddEventTypeInteractorProtocol
extension CalendarAddEventTypeInteractor: CalendarAddEventTypeInteractorProtocol {
    func addTaskType(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CalendarService.addTaskType(request), responseType: EventTypeDetailResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didAddTaskType(response.detail ?? EventTypeModel())
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
