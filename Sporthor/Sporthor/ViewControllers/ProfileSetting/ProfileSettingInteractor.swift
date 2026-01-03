//
//  ProfileSettingInteractor.swift
//  Sporthor
//
//  Created by derTurke on 16.08.2025.
//
//

import Foundation
import Factory

final class ProfileSettingInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ProfileSettingInteractorDelegate? {
        get {
            return self.baseDelegate as? ProfileSettingInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - ProfileSettingInteractorProtocol
extension ProfileSettingInteractor: ProfileSettingInteractorProtocol {
    func blockUser(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.addBlockUser(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didBlockUser()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
