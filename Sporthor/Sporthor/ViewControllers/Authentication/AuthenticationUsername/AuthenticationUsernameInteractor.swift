//
//  AuthenticationUsernameInteractor.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationUsernameInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationUsernameInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationUsernameInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    private var networkManager: NetworkKitProtocol
    override init() {
        networkManager = NetworkManager()
        super.init()
    }
}

// MARK: - AuthenticationUsernameInteractorProtocol
extension AuthenticationUsernameInteractor: AuthenticationUsernameInteractorProtocol {
    func checkUsername(_ request: [String: Any]) async {
        let result = await networkManager.request(service: AuthenticationService.checkUsername(request),
                                                  responseType: CheckUsernameResponse.self)
        switch result {
        case .success(let response):
            delegate?.didCheckUsername(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func register(_ request: [String: Any]) async {
        let result = await networkManager.request(service: AuthenticationService.register(request), responseType: AuthResponse.self)
        switch result {
        case .success(let response):
            delegate?.didRegister(response)
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
