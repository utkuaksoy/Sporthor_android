//
//  CalendarAddPersonOrGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import Foundation
import Factory

final class CalendarAddPersonOrGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarAddPersonOrGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarAddPersonOrGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarAddPersonOrGroupInteractorProtocol
extension CalendarAddPersonOrGroupInteractor: CalendarAddPersonOrGroupInteractorProtocol {
    func getTrainingGroupUser() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CoachService.getTrainingGroupUser, responseType: GetTrainingGroupUserResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didGetTrainingGroupUser(response.groups)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
