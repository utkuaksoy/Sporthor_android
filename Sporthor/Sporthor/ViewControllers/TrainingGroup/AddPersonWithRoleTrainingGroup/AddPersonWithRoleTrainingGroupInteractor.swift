//
//  AddPersonWithRoleTrainingGroupInteractor.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import Foundation
import Factory

final class AddPersonWithRoleTrainingGroupInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AddPersonWithRoleTrainingGroupInteractorDelegate? {
        get {
            return self.baseDelegate as? AddPersonWithRoleTrainingGroupInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - AddPersonWithRoleTrainingGroupInteractorProtocol
extension AddPersonWithRoleTrainingGroupInteractor: AddPersonWithRoleTrainingGroupInteractorProtocol {
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
    
    func getFollowing(userId: String, role: Int) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: FollowersService.fetchFollowing(userId: userId, role: role),
            responseType: FollowersResponse.self,
            showLoading: false
        )
        switch result {
        case .success(let response):
            delegate?.didGetFollowing(response.users)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addTrainingGroupUser(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(
            service: CoachService.addTrainingGroupUser(request),
            responseType: NullResponse.self
        )
        switch result {
        case .success(let response):
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
