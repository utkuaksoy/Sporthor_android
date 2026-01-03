//
//  AccountSettingsInteractor.swift
//  Sporthor
//
//  Created by derTurke on 31.07.2025.
//
//

import Foundation
import Factory

final class AccountSettingsInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AccountSettingsInteractorDelegate? {
        get {
            return self.baseDelegate as? AccountSettingsInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - AccountSettingsInteractorProtocol
extension AccountSettingsInteractor: AccountSettingsInteractorProtocol {
    func deleteAccount() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: AuthenticationService.deleteAccount, responseType: NullResponse.self)
        
        switch result {
        case .success(_):
            baseLogout()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func updateProfilePublicPrivate(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: ProfileService.updateProfilePublicPrivate(request), responseType: NullResponse.self)
        
        switch result {
        case .success(_):
            delegate?.didUpdateProfilePublicPrivate()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
