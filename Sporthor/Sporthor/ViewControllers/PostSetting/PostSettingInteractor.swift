//
//  PostSettingInteractor.swift
//  Sporthor
//
//  Created by derTurke on 6.05.2025.
//
//

import Foundation
import Factory

final class PostSettingInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: PostSettingInteractorDelegate? {
        get {
            return self.baseDelegate as? PostSettingInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - PostSettingInteractorProtocol
extension PostSettingInteractor: PostSettingInteractorProtocol {
    func hidePost(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.hidePost(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didHideOrDeletePost()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func deletePost(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: SocialService.deletePost(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(_):
            delegate?.didHideOrDeletePost()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
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
