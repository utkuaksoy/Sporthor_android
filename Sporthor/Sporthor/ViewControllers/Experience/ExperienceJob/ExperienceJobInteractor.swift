//
//  ExperienceJobInteractor.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import Foundation
import Factory

final class ExperienceJobInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ExperienceJobInteractorDelegate? {
        get {
            return self.baseDelegate as? ExperienceJobInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - ExperienceJobInteractorProtocol
extension ExperienceJobInteractor: ExperienceJobInteractorProtocol {
    func getMyRoles() async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ProfileService.getMyRoles,
            responseType: GetMyRolesResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didGetMyRoles(response.roles)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateUserRoles(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ProfileService.updateUserRoles(request),
            responseType: GetMyRolesResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didUpdateUserRoles(response.roles)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
