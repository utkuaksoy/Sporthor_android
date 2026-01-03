//
//  ListClubInteractor.swift
//  Sporthor
//
//  Created by derTurke on 3.07.2025.
//
//

import Foundation
import Factory

final class ListClubInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ListClubInteractorDelegate? {
        get {
            return self.baseDelegate as? ListClubInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - ListClubInteractorProtocol
extension ListClubInteractor: ListClubInteractorProtocol {
    func getSportClub() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ManagerService.getSportClub, responseType: [SportClub].self)
        
        switch result {
        case .success(let response):
            delegate?.didGetSportClub(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func removeSportClub(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ManagerService.removeSportClub(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(_):
            delegate?.didRemoveSportClub()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
