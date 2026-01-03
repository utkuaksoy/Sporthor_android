//
//  ProfileInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.02.2025.
//
//

import ComponentBaseKit
import Factory
import Foundation
import NetworkKit

final class ProfileInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: ProfileInteractorDelegate? {
        get {
            return self.baseDelegate as? ProfileInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager) private var networkManager
        
    var viewModel: ProfileViewModel
    
    init(viewModel: ProfileViewModel) {
        self.viewModel = viewModel
    }
}

// MARK: - ProfileInteractorProtocol
extension ProfileInteractor: ProfileInteractorProtocol {
    
    func fetchProfileService(userId: String?, isUpdate: Bool) async {
        guard let networkManager else { return }
        let request = ProfileService.fetchProfile(userId: userId)
        let result = await networkManager.request(service: request, responseType: ProfileResponseModel.self)
        
        switch result {
        case .success(let response):
            viewModel.update(with: response)
            self.delegate?.fetchProfileServiceSuccess(response: response, isUpdate: isUpdate)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func followUser(_ userId: String) {
        Task {
            guard let networkManager else { return }
            let request = FollowersService.followUser(userId: userId)
            let result = await networkManager.request(service: request, responseType: FollowAndFollowingCountResponse.self)
            
            switch result {
            case .success(_):
                delegate?.didFollowUser(userId)
            case .failure(let error):
                self.delegate?.didFailure(error)
            }
        }
    }
    
    func unfollowUser(_ userId: String) {
        Task {
            guard let networkManager else { return }
            let request = FollowersService.unFollowUser(userId: userId)
            let result = await networkManager.request(service: request, responseType: FollowAndFollowingCountResponse.self)
            
            switch result {
            case .success(_):
                delegate?.didUnfollowUser(userId)
            case .failure(let error):
                self.delegate?.didFailure(error)
            }
        }
    }
    
    func createGroup(
        name: String,
        selectedUserId: String,
        groupImagePath: String?,
        userName: String
    ) async {
        guard let networkManager else { return }
        let request = GroupChatRequestModel(
            name: name,
            image: groupImagePath,
            users: [selectedUserId],
            isPrivate: true
        )
        let result = await networkManager.request(
            service: GroupChatNetworkTask.createGroupChat(
                request: request
            ),
            responseType: GroupChatResponseModel.self
        )
        switch result {
        case .success(let response):
            self.delegate?.didCreateGroupSuccess(response: response, userName: userName)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
}
