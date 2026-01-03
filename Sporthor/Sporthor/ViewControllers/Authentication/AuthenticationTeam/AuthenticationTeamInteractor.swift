//
//  AuthenticationTeamInteractor.swift
//  Sporthor
//
//  Created by derTurke on 24.02.2025.
//
//

import Foundation

final class AuthenticationTeamInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationTeamInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationTeamInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    private let networkManager: NetworkKitProtocol
    
    override init() {
        networkManager = NetworkManager()
    }
}

// MARK: - AuthenticationTeamInteractorProtocol
extension AuthenticationTeamInteractor: AuthenticationTeamInteractorProtocol {
    func getTeams() async {
        let result = await networkManager.request(service: TeamService.getSporthorTeams,
                                                  responseType: TeamResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didGetTeams(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func saveUserTeams(_ request: [String : Any]) async {
        let result = await networkManager.request(service: TeamService.saveUserTeams(request),
                                                  responseType: SaveUserTeamResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didSaveUserTeams(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
