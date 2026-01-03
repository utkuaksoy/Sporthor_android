//
//  AuthenticationVerifyCodeInteractor.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationVerifyCodeInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationVerifyCodeInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationVerifyCodeInteractorDelegate
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

// MARK: - AuthenticationVerifyCodeInteractorProtocol
extension AuthenticationVerifyCodeInteractor: AuthenticationVerifyCodeInteractorProtocol {
    func loginWithPhone(_ request: [String: Any]) async {
        let result = await networkManager.request(service: AuthenticationService.valideOtp(request),
                                                  responseType: AuthResponse.self)
        
        switch result {
        case .success(let response):
            delegate?.didLoginWithPhone(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func validateOtp(_ request: [String: Any]) async {
        let result = await networkManager.request(service: AuthenticationService.valideOtp(request),
                                                  responseType: NullResponse.self)
        
        switch result {
        case .success(_):
            delegate?.didValidateOtp()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
