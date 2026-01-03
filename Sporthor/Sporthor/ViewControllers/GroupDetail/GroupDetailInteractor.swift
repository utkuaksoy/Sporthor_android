//
//  GroupDetailInteractor.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 9.04.2025.
//
//

import Factory
import Foundation
import NetworkKit

final class GroupDetailInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: GroupDetailInteractorDelegate? {
        get {
            return self.baseDelegate as? GroupDetailInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager)
    private var networkManager
    
    private let groupId: String
    
    // MARK: - Initialize
    
    init(groupId: String) {
        self.groupId = groupId
    }
}

// MARK: - GroupDetailInteractorProtocol
extension GroupDetailInteractor: GroupDetailInteractorProtocol {
    
    func fetchGroupDetailService(with groupId: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: GroupDetailServiceNetworkTask.getGroupChatService(groupId: groupId),
            responseType: GroupDetailResponseModel.self
        )
        switch result {
        case .success(let response):
            delegate?.fetchGroupDetailSuccess(groupDetailResponse: response)
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func leaveGroup() async {
       
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: GroupDetailServiceNetworkTask.leaveGroup(groupId: groupId),
            responseType: Bool.self
        )
        switch result {
        case .success:
            delegate?.leaveGroupSuccess()
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
    }
    
    func removeMember(userId: String) async {
        // Simüle edilmiş network gecikmesi
//        try? await Task.sleep(nanoseconds: 1_000_000_000) // 1 saniye
//        if let index = mockData.members.firstIndex(where: { $0.id == userId }) {
//            delegate?.removeMemberSuccess(at: IndexPath(row: index, section: 0))
//        }
        
        // TODO: Real network implementation
        /*
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: GroupDetailServiceNetworkTask.removeMember(groupId: groupId, userId: userId),
            responseType: Bool.self
        )
        switch result {
        case .success:
            delegate?.removeMemberSuccess(at: IndexPath(row: 0, section: 0))
        case .failure(let error):
            self.delegate?.didFailure(error)
        }
        */
    }
    
    func followUser(_ userId: String) {
        Task {
            guard let networkManager else { return }
            let request = FollowersService.followUser(userId: userId)
            let result = await networkManager.request(service: request, responseType: EmptyResponse.self)
            
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
            let result = await networkManager.request(service: request, responseType: EmptyResponse.self)
            
            switch result {
            case .success(_):
                delegate?.didUnfollowUser(userId)
            case .failure(let error):
                self.delegate?.didFailure(error)
            }
        }
    }
}
