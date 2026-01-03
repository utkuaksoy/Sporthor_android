//
//  CalendarMissionDraftInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//
//

import Foundation
import Factory

final class CalendarMissionDraftInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarMissionDraftInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarMissionDraftInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarMissionDraftInteractorProtocol
extension CalendarMissionDraftInteractor: CalendarMissionDraftInteractorProtocol {
    func getDrafts() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CalendarService.getDrafts, responseType: GetCalendarDetailResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didGetDrafts(response.tasks)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
