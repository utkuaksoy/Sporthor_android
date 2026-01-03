//
//  ListTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//
//

import Foundation
import Factory

final class ListTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ListTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? ListTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - ListTrainingGroupInteractorProtocol
extension ListTrainingGroupInteractor: ListTrainingGroupInteractorProtocol {
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
    
    func removeTrainingGroup(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: CoachService.removeTrainingGroup(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(_):
            delegate?.didRemoveTrainingGroup()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
