//
//  CalendarRPEInteractor.swift
//  Sporthor
//
//  Created by derTurke on 25.07.2025.
//
//

import Foundation
import Factory

final class CalendarRPEInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarRPEInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarRPEInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarRPEInteractorProtocol
extension CalendarRPEInteractor: CalendarRPEInteractorProtocol {
    func rpeSurvey(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: CalendarService.rpeSurvey(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(_):
            delegate?.didRPESurvey()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
