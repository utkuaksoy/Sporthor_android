//
//  AccountBlockUserListInteractor.swift
//  Sporthor
//
//  Created by derTurke on 15.08.2025.
//
//

import Foundation
import Factory

final class AccountBlockUserListInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AccountBlockUserListInteractorDelegate? {
        get {
            return self.baseDelegate as? AccountBlockUserListInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - AccountBlokeListInteractorProtocol
extension AccountBlockUserListInteractor: AccountBlockUserListInteractorProtocol {
    func getBlockUser() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: SocialService.getBlockUser, responseType: GetBlockUserResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didGetBlockUser(response.users)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func blockUser(_ request: [String: Any], isAddBlock: Bool) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: isAddBlock ? SocialService.addBlockUser(request) : SocialService.removeBlockUser(request),
            responseType: NullResponse.self
        )
        
        switch result {
        case .success(_):
            delegate?.didBlockUser(isAddBlock: isAddBlock)
        case .failure(let error):
            delegate?.didFailure(error)
        }

    }
}
