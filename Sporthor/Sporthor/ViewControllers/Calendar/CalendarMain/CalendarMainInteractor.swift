//
//  CalendarMainInteractor.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation
import Factory

final class CalendarMainInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarMainInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarMainInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarMainInteractorProtocol
extension CalendarMainInteractor: CalendarMainInteractorProtocol {
    func getCalendar(_ date: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CalendarService.getCalendar(date), responseType: CalendarEventResponse.self, showLoading: false)
        
        switch result {
        case .success(let response):
            delegate?.didGetCalendar(response.events)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
