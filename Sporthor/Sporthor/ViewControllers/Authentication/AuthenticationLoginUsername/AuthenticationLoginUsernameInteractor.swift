//
//  AuthenticationLoginUsernameInteractor.swift
//  Sporthor
//
//  Created by derTurke on 19.02.2025.
//
//

import Foundation

final class AuthenticationLoginUsernameInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationLoginUsernameInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationLoginUsernameInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    let networkManager: NetworkKitProtocol
    
    override init() {
        self.networkManager = NetworkManager()
        super.init()
    }
}

// MARK: - AuthenticationLoginUsernameInteractorProtocol
extension AuthenticationLoginUsernameInteractor: AuthenticationLoginUsernameInteractorProtocol {
    func loginWithUsername(_ request: [String : Any]) async {
        let result = await networkManager.request(service: AuthenticationService.loginWithUsername(request),
                                                  responseType: AuthResponse.self)
        switch result {
        case .success(let response):
            delegate?.didLoginWithUsername(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getConfiguration() async {
        let result = await networkManager.request(
            service: ConfigurationService.getConfiguration,
            responseType: GetConfigurationResponse.self
        )
        switch result {
        case .success(let response):
            delegate?.didGetConfiguration(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
