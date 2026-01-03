//
//  AuthenticationPhoneInteractor.swift
//  Sporthor
//
//  Created by derTurke.
//
//

import Foundation

final class AuthenticationPhoneInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: AuthenticationPhoneInteractorDelegate? {
        get {
            return self.baseDelegate as? AuthenticationPhoneInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    var networkManager: NetworkKitProtocol
    
    override init() {
        self.networkManager = NetworkManager()
    }
}

// MARK: - AuthenticationPhoneInteractorProtocol
extension AuthenticationPhoneInteractor: AuthenticationPhoneInteractorProtocol {
    func generateOtp(request: [String: Any]) async {
        let result = await networkManager.request(service: AuthenticationService.generateOtp(request), responseType: GenerateOtpResponse.self)
        
        switch result {
        case .success(let response):
            print("\n\nValidate OTP" + (response.code ?? "") + "\n\n")
            delegate?.didGenerateOtp(response)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
