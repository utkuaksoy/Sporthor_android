//
//  ChatUserInfoInteractor.swift
//  Sporthor
//
//  Created by Mdsut Canbaz on 12.04.2025.
//
//

import Factory
import Foundation
import NetworkKit

final class ChatUserInfoInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ChatUserInfoInteractorDelegate?
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager) private var networkManager
    private var users: [CreateChatUserModel] = []
}

// MARK: - ChatUserInfoInteractorProtocol
extension ChatUserInfoInteractor: ChatUserInfoInteractorProtocol {
    func fetchUserInfo(userId: String, groupId: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: ChatUserInfoNetworkTask.getChatUserProfile(userId: userId, groupId: groupId),
            responseType: ChatUserInfoResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.userInfoFetched(response)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func followUser(userId: String) {
        guard let networkManager else { return }
        Task {
            let request = FollowersService.followUser(userId: userId)
            let result = await networkManager.request(service: request, responseType: FollowAndFollowingCountResponse.self)
            
            switch result {
            case .success(_):
                FollowingManager.shared.updateFollowingStatus(userId: userId, followStatus: .following)
                delegate?.didFollowUser(userId)
            case .failure(let error):
                self.delegate?.didFailure(error)
            }
        }
    }
    
    func unfollowUser(userId: String) {
        Task {
            guard let networkManager else { return }
            let request = FollowersService.unFollowUser(userId: userId)
            let result = await networkManager.request(service: request, responseType: FollowAndFollowingCountResponse.self)
            
            switch result {
            case .success(_):
                FollowingManager.shared.updateFollowingStatus(userId: userId, followStatus: .follow)
                delegate?.didUnfollowUser(userId)
            case .failure(let error):
                self.delegate?.didFailure(error)
            }
        }
    }
}
