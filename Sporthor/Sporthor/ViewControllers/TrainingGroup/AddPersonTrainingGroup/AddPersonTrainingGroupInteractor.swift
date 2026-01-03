//
//  AddPersonTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation
import Factory

final class AddPersonTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AddPersonTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? AddPersonTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - AddPersonTrainingGroupInteractorProtocol
extension AddPersonTrainingGroupInteractor: AddPersonTrainingGroupInteractorProtocol {
    func search(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: SocialService.search(request), responseType: SearchesResponse.self, showLoading: false)
        switch result {
        case .success(let response):
            delegate?.didSearch(response.searchList)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addTrainingGroupUser(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CoachService.addTrainingGroupUser(request), responseType: NullResponse.self)
        switch result {
        case .success(_):
            delegate?.didAddTrainingGroupUser()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateCoach(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ManagerService.updateCoach(request), responseType: NullResponse.self)
        switch result {
        case .success(_):
            delegate?.didAddTrainingGroupUser()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
