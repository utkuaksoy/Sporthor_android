//
//  FollowersInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 2.04.2025.
//
//

import Factory
import NetworkKit
import UIKit

final class FollowersInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: FollowersInteractorDelegate? {
        get {
            return self.baseDelegate as? FollowersInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties

    private var followers: [FollowerModel] = []
    private var following: [FollowerModel] = []
    private var mutual: [FollowerModel] = []
    private var currentDirection: FollowDirectionEnum = .followers
    
    @LazyInjected(\.networkManager) private var networkManager
    
    // MARK: - Initialize
    init(direction: FollowDirectionEnum) {
        super.init()
        self.currentDirection = direction
    }
}

// MARK: - FollowersInteractorProtocol
extension FollowersInteractor: FollowersInteractorProtocol {
    
    func fetchFollowers(userId: String) async {
        guard let networkManager else { return }
        let request = FollowersService.fetchFollowers(userId: userId)
        let result = await networkManager.request(service: request, responseType: FollowersResponse.self)
        
        switch result {
        case .success(let response):
            self.delegate?.didFetchFollowers(response.users)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func fetchFollowing(userId: String) async {
        guard let networkManager else { return }
        let request = FollowersService.fetchFollowing(userId: userId)
        let result = await networkManager.request(service: request, responseType: FollowersResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didFetchFollowing(response.users)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func fetchFollowTogether(userId: String) async {
        guard let networkManager else { return }
        let request = FollowersService.fetchFollowTogether(userId: userId)
        let result = await networkManager.request(service: request, responseType: FollowersResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didFetchFollowTogether(response.users)
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
                updateFollowStatus(userId: userId, isFollowing: true)
                FollowingManager.shared.updateFollowingStatus(userId: userId, followStatus: .following)
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
                updateFollowStatus(userId: userId, isFollowing: false)
                FollowingManager.shared.updateFollowingStatus(userId: userId, followStatus: .follow)
                delegate?.didUnfollowUser(userId)
            case .failure(let error):
                self.delegate?.didFailure(error)
            }
        }
    }
    
    private func updateFollowStatus(userId: String, isFollowing: Bool) {
        if let index = followers.firstIndex(where: { $0.id == userId }) {
            followers[index].isFollow = isFollowing
        }
        if let index = following.firstIndex(where: { $0.id == userId }) {
            following[index].isFollow = isFollowing
        }
        if let index = mutual.firstIndex(where: { $0.id == userId }) {
            mutual[index].isFollow = isFollowing
        }
        if isFollowing {
            delegate?.didFollowUser(userId)
        } else {
            delegate?.didUnfollowUser(userId)
        }
    }
}
